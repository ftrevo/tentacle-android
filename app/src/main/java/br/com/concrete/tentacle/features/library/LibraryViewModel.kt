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

class LibraryViewModel(private val libraryRepository: LibraryRepository) : BaseViewModel() {

    private val viewStateLibrary: MutableLiveData<Event<ViewStateModel<LibraryResponse>>> = MutableLiveData()
    private val libraryMore: MutableLiveData<Event<ViewStateModel<LibraryResponse>>> = MutableLiveData()

    fun getLibrary() = viewStateLibrary
    fun getLibraryMore() = libraryMore

    var page: Int = 1

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun preLoadLibrary() {
        loadLibrary()
    }

    fun loadLibrary(queryParameters: QueryParameters? = null, search: String? = null, filtering: Boolean = false) {
        val query = queryParameters ?: QueryParameters()

        viewStateLibrary.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(
            libraryRepository.getLibrary(query, search)
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

    fun loadLibraryMore(
        queryParameters: QueryParameters? = null,
        search: String? = null,
        filtering: Boolean = false
    ) {

        val query = queryParameters ?: QueryParameters()
        query.page = page

        disposables.add(
            libraryRepository.getLibrary(query, search)
                .subscribe({ baseModel ->
                    libraryMore.postValue(
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
                    libraryMore.postValue(
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

    fun initPage() {
        this.page = 1
    }
}