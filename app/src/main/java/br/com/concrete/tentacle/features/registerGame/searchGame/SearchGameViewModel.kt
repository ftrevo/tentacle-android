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
import br.com.concrete.tentacle.utils.Event

class SearchGameViewModel(
    private val gameRepository: GameRepository
) : BaseViewModel(),
    LifecycleObserver {

    private val viewSearchGame = MutableLiveData<ViewStateModel<GameResponse>>()
    private val viewGame = MutableLiveData<Event<ViewStateModel<Game>>>()
    private val viewGameMore = MutableLiveData<Event<ViewStateModel<GameResponse>>>()

    val game: LiveData<Event<ViewStateModel<Game>>>
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
        viewGame.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(obsRegisterGame(name))
    }

    private fun obsRegisterGame(name: String) =
        gameRepository.registerNewGame(GameRequest(name)).subscribe({ base ->
            viewGame.postValue(
                Event(
                    ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = base.data
                    )
                )
            )
        }, {
            viewGame.postValue(
                Event(
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
                Event(
                    ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = base.data
                    )
                )
            )
            page += 1
        }, {
            viewGameMore.postValue(
                Event(
                    ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it)
                    )
                )
            )
        })

    fun getSearchGame(): LiveData<ViewStateModel<GameResponse>> = viewSearchGame
    fun getRegisteredGame(): LiveData<Event<ViewStateModel<Game>>> = viewGame
    fun getRegisteredGameMore(): LiveData<Event<ViewStateModel<GameResponse>>> = viewGameMore
    fun onePage() {
        page = 1
    }
}