package br.com.concrete.tentacle.features.registerGame.remoteGame

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.Event
import br.com.concrete.tentacle.utils.LogWrapper

class RemoteGameViewModel(private val gameRepository: GameRepository) : BaseViewModel() {

    val remoteGamesViewState = MutableLiveData<ViewStateModel<ArrayList<Game>>>()
    val gameViewState = MutableLiveData< Event<ViewStateModel<Game>>>()

    fun getRemoteGames(name: String) {
        remoteGamesViewState.postValue(ViewStateModel(ViewStateModel.Status.LOADING))

        disposables.add(
            gameRepository.loadRemoteGames(name).subscribe({ base ->
                if (base.data.list.isNotEmpty()) {
                    base.data.list.add(Game.getEmptyGame())
                } else {
                    base.data.list
                }

                remoteGamesViewState.postValue(
                    ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = ArrayList(base.data.list)
                    )
                )
            }, {
                remoteGamesViewState.postValue(
                    ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it)
                    )
                )
            })
        )
    }

    fun registerRemoteGame(gameId: Int) {
        gameViewState.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(
            gameRepository.registerRemoteGame(GameRequest(id = gameId)).subscribe({ base ->
                gameViewState.postValue(
                    Event(ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = base.data)
                    )
                )
            }, {
                gameViewState.postValue(
                    Event(ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it))
                    )
                )
                LogWrapper.print(it)
            })
        )
    }
}