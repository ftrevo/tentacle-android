package br.com.concrete.tentacle.features.library

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.repositories.LibraryRepository

class LibraryViewModel(private val libraryRepository: LibraryRepository) : BaseViewModel() {

    private val viewStateLibrary: MutableLiveData<ViewStateModel<ArrayList<Library>>> = MutableLiveData()
    fun getLibrary() = viewStateLibrary

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun preLoadLibrary() {
        loadLibrary()
    }

    fun loadLibrary(queryParameters: QueryParameters? = null) {
        val query = queryParameters ?: QueryParameters()

        viewStateLibrary.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            libraryRepository.getLibrary(query)
                .subscribe({ baseModel ->
                    viewStateLibrary.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = baseModel.data.list as ArrayList<Library>
                        )
                    )
                }, {
                    viewStateLibrary.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                })
        )
    }
}