package br.com.concrete.tentacle.features.searchGame

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.testing.OpenForTesting
import io.reactivex.disposables.CompositeDisposable

@OpenForTesting
class SearchGameViewModel(
    private val gameRepository: GameRepository
) : BaseViewModel(),
    LifecycleObserver {

    private val viewSearchGame = MutableLiveData<ViewStateModel<ArrayList<Game>>>()
    private val viewGame = MutableLiveData<ViewStateModel<Game>>()

    fun searchGame(title: String) {
        viewSearchGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(obsSearchGames(title))
    }

    private fun obsSearchGames(title: String) =
        gameRepository.getSearchGames(title).subscribe({ base ->

            Log.i("TESTE TESTE", base.data.list[0].title)
            viewSearchGame.postValue(
                ViewStateModel(
                    status = ViewStateModel.Status.SUCCESS,
                    model = base.data.list as ArrayList<Game>
                )
            )
        }, {

            Log.i("TESTE TESTE", "ERROR")
            viewSearchGame.postValue(
                ViewStateModel(
                    status = ViewStateModel.Status.ERROR,
                    errors = notKnownError(it)
                )
            )
        })

    fun registerNewGame(title: String) {
        viewGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(obsRegisterGame(title))
    }

    private fun obsRegisterGame(title: String) =
        gameRepository.registerNewGame(GameRequest(title)).subscribe({ base ->
                viewGame.postValue(
                    ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = base.data
                    )
                )
            }, {
                viewGame.postValue(
                    ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it)
                    )
                )
            })

    fun getSearchGame() = viewSearchGame
    fun getRegisteredGame() = viewGame

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}