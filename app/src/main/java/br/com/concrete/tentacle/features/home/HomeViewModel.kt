package br.com.concrete.tentacle.features.home

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository

class HomeViewModel(private val gameRepository: GameRepository) : BaseViewModel() {

    private val viewStateGame: MutableLiveData<ViewStateModel<ArrayList<Game>>> = MutableLiveData()
    fun getHomeGames() = viewStateGame

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun loadHomeGames() {
        viewStateGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(gameRepository.loadHomeGames()
            .subscribe({ baseModel ->
                viewStateGame.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data.list as ArrayList<Game>))
            }, {
                viewStateGame.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }
}