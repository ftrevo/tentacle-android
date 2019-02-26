package br.com.concrete.tentacle.features.library

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.repositories.LibraryRepository
import br.com.concrete.tentacle.utils.Event

class LibraryViewModel(private val libraryRepository: LibraryRepository) : BaseViewModel() {

    private val viewStateLibrary: MutableLiveData<Event<ViewStateModel<LibraryResponse>>> = MutableLiveData()
    private val libraryMore: MutableLiveData<Event<ViewStateModel<LibraryResponse>>> = MutableLiveData()

    fun getLibrary() = viewStateLibrary
    fun getLibraryMore() = libraryMore

    var page: Int = 0

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun preLoadLibrary() {
        loadLibrary()
    }

    fun loadLibrary(queryParameters: QueryParameters? = null, search: String? = null, filtering: Boolean = false) {
        val query = queryParameters ?: QueryParameters()

        viewStateLibrary.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(
            library(query, search)
        )
    }

    fun loadLibraryMore(
        queryParameters: QueryParameters? = null,
        search: String? = null,
        filtering: Boolean = false
    ) {

        val query = queryParameters ?: QueryParameters()
        query.page = page

        viewStateLibrary.postValue(
            Event(
                ViewStateModel(
                    ViewStateModel.Status.LOADING
                )
            )
        )

        disposables.add(
            library(query, search, filtering)
        )
    }

    private fun library(
        queryParameters: QueryParameters,
        search: String? = null,
        filtering: Boolean = false
    ) = libraryRepository.getLibrary(queryParameters, search)
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