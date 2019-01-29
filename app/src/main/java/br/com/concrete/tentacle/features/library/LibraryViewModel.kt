package br.com.concrete.tentacle.features.library

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.repositories.GameRepository

class LibraryViewModel(private val gameRepository: GameRepository) : BaseViewModel() {

    private val viewStateLibrary: MutableLiveData<ViewStateModel<ArrayList<Library>>> = MutableLiveData()
    fun getLibrary() = viewStateLibrary

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun loadLibrary() {
        viewStateLibrary.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(gameRepository.getLibrary()
            .subscribe({ baseModel ->
                viewStateLibrary.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data.list as ArrayList<Library>))
            }, {
                viewStateLibrary.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }
}
