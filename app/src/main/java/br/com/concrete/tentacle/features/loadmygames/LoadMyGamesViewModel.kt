package br.com.concrete.tentacle.features.loadmygames

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository

class LoadMyGamesViewModel(private val gameRepository: GameRepository): BaseViewModel() {

    private val viewStateGame: MutableLiveData<ViewStateModel<ArrayList<Media>>> = MutableLiveData()
    fun getMyGames() = viewStateGame

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadMyGames(){
        viewStateGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(gameRepository.loadMyGames()
            .subscribe({ baseModel ->
                viewStateGame.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data.list as ArrayList<Media>))
            },{
                viewStateGame.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }

}
