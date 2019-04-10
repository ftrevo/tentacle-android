package br.com.concrete.tentacle.features.registerGame.remoteGame

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.SingleEvent
import br.com.concrete.tentacle.utils.LogWrapper

class RemoteGameViewModel(private val gameRepository: GameRepository) : BaseViewModel() {

    val remoteGamesViewState = MutableLiveData<ViewStateModel<ArrayList<Game>>>()
    val remoteGamesMoreViewState = MutableLiveData<ViewStateModel<ArrayList<Game>>>()
    val gameViewState = MutableLiveData< SingleEvent<ViewStateModel<Game>>>()

    var name: String = ""
    var page = 1

    fun getRemoteGames(name: String) {
        this.name = name
        remoteGamesViewState.postValue(ViewStateModel(ViewStateModel.Status.LOADING))

        disposables.add(
            gameRepository.loadRemoteGames(gameName = name).subscribe({ base ->

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
        gameViewState.postValue(SingleEvent(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(
            gameRepository.registerRemoteGame(GameRequest(id = gameId)).subscribe({ base ->
                gameViewState.postValue(
                    SingleEvent(ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = base.data)
                    )
                )
            }, {
                gameViewState.postValue(
                    SingleEvent(ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it))
                    )
                )
                LogWrapper.print(it)
            })
        )
    }

    fun loadMore() {
        remoteGamesMoreViewState.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            gameRepository.loadRemoteGames(gameName = name, page = page).subscribe({ base ->
                if (base.data.list.isNotEmpty()) {
                    page++
                } else {
                    base.data.list
                }

                remoteGamesMoreViewState.postValue(
                    ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = ArrayList(base.data.list)
                    )
                )
            }, {
                remoteGamesMoreViewState.postValue(
                    ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it)
                    )
                )
            })
        )
    }
}