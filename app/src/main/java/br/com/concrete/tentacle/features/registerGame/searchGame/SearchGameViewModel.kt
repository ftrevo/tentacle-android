package br.com.concrete.tentacle.features.registerGame.searchGame

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.SingleEvent

class SearchGameViewModel(
    private val gameRepository: GameRepository
) : BaseViewModel(),
    LifecycleObserver {

    private val viewSearchGame = MutableLiveData<ViewStateModel<GameResponse>>()
    private val viewGame = MutableLiveData<SingleEvent<ViewStateModel<Game>>>()
    private val viewGameMore = MutableLiveData<SingleEvent<ViewStateModel<GameResponse>>>()

    val game: LiveData<SingleEvent<ViewStateModel<Game>>>
        get() = viewGame

    private var page = 1

    fun searchGame(title: String) {
        viewSearchGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(obsSearchGames(title))
    }

    private fun obsSearchGames(name: String) =
        gameRepository.getSearchGames(name).subscribe({ base ->
            viewSearchGame.postValue(
                ViewStateModel(
                    status = ViewStateModel.Status.SUCCESS,
                    model = base.data
                )
            )
        }, {
            viewSearchGame.postValue(
                ViewStateModel(
                    status = ViewStateModel.Status.ERROR,
                    errors = notKnownError(it)
                )
            )
        })

    fun registerNewGame(name: String) {
        viewGame.postValue(SingleEvent(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(obsRegisterGame(name))
    }

    private fun obsRegisterGame(name: String) =
        gameRepository.registerNewGame(GameRequest(name = name)).subscribe({ base ->
            viewGame.postValue(
                SingleEvent(
                    ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = base.data
                    )
                )
            )
        }, {
            viewGame.postValue(
                SingleEvent(
                    ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it)
                    )
                )
            )
        })

    fun searchGameMore(title: String) {
        disposables.add(obsSearchGameMore(title))
    }

    private fun obsSearchGameMore(title: String) =
        gameRepository.getSearchGames(title, page).subscribe({ base ->
            viewGameMore.postValue(
                SingleEvent(
                    ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = base.data
                    )
                )
            )
            page += 1
        }, {
            viewGameMore.postValue(
                SingleEvent(
                    ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it)
                    )
                )
            )
        })

    fun getSearchGame(): LiveData<ViewStateModel<GameResponse>> = viewSearchGame
    fun getRegisteredGame(): LiveData<SingleEvent<ViewStateModel<Game>>> = viewGame
    fun getSearchMoreGame(): LiveData<SingleEvent<ViewStateModel<GameResponse>>> = viewGameMore
    fun onePage() {
        page = 1
    }
}