package br.com.concrete.tentacle.features.library

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.repositories.LibraryRepository
import br.com.concrete.tentacle.utils.Event
import io.reactivex.disposables.Disposable

class LibraryViewModel(private val libraryRepository: LibraryRepository) : BaseViewModel() {

    private val viewStateLibrary: MutableLiveData<Event<ViewStateModel<LibraryResponse>>> = MutableLiveData()
    fun getLibrary() = viewStateLibrary

    var page: Int = 0

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun preLoadLibrary() {
        loadLibrary()
    }

    fun loadLibrary(queryParameters: QueryParameters? = null, search: String? = null, filtering: Boolean = false) {
        val query = queryParameters ?: QueryParameters()

        viewStateLibrary.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(getObservable(query, search, filtering))
    }

    fun loadMoreLibrary(queryParameters: QueryParameters? = null, search: String? = null, filtering: Boolean = false) {
        val query = queryParameters ?: QueryParameters()
        query.page = page
        disposables.add(getObservable(query, search, filtering))
    }

    private fun getObservable(queryParameters: QueryParameters, search: String? = null, filtering: Boolean = false): Disposable {
        return libraryRepository.getLibrary(queryParameters, search)
            .subscribe({ baseModel ->
                viewStateLibrary.postValue(
                    Event(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = baseModel.data,
                            filtering = filtering
                        )
                    )
                )
                page += 1
            }, {
                viewStateLibrary.postValue(
                    Event(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                )
            })
    }

    fun initPage() {
        this.page = 0
    }
}