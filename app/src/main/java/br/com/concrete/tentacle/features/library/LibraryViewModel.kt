package br.com.concrete.tentacle.features.library

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.repositories.LibraryRepository

class LibraryViewModel(private val libraryRepository: LibraryRepository) : BaseViewModel() {
    private val viewStateLibray: MutableLiveData<ViewStateModel<ArrayList<Library>>> = MutableLiveData()
    fun getLibrary() = viewStateLibray

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun loadLibrary() {
        viewStateLibray.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(libraryRepository.getLibrary()
            .subscribe({ baseModel ->
                viewStateLibray.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data.list as ArrayList<Library>))
            }, {
                viewStateLibray.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }
}