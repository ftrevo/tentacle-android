package br.com.concrete.tentacle.features.library

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.repositories.LibraryRepository
import br.com.concrete.tentacle.utils.Event

class LibraryViewModel(private val libraryRepository: LibraryRepository) : BaseViewModel() {

    private val viewStateLibrary: MutableLiveData<Event<ViewStateModel<ArrayList<Library>>>> = MutableLiveData()
    fun getLibrary() = viewStateLibrary

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun preLoadLibrary() {
        loadLibrary()
    }

    fun loadLibrary(queryParameters: QueryParameters? = null, search: String? = null) {
        val query = queryParameters ?: QueryParameters()

        viewStateLibrary.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(
            libraryRepository.getLibrary(query, search)
                .subscribe({ baseModel ->
                    viewStateLibrary.postValue(
                        Event(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data.list as ArrayList<Library>
                            )
                        )
                    )
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
        )
    }
}