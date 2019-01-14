package br.com.concrete.tentacle.features.searchGame

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import io.reactivex.disposables.CompositeDisposable

class SearchGameViewModel(
    private val gameRepository: GameRepository,
    private val sharedPrefRepository: SharedPrefRepository
)
    : BaseViewModel(),
    LifecycleObserver {

    private val viewSearchGame = MutableLiveData<ViewStateModel<ArrayList<Game>>>()
    private val viewGame = MutableLiveData<ViewStateModel<ArrayList<Game>>>()
    private val disposables = CompositeDisposable()

    fun searchGame(title: String) {
        viewSearchGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        val session: Session = sharedPrefRepository.getStoredSession(PREFS_KEY_USER_SESSION)
        disposables.add(gameRepository.getSearchGames(title, session.accessToken).subscribe({ base ->
            viewSearchGame.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data.list as ArrayList<Game>))
        }, {
            viewSearchGame.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
        }))
    }

    fun registerNewGame(title: String) {
        viewGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        val session: Session = sharedPrefRepository.getStoredSession(PREFS_KEY_USER_SESSION)
        disposables.add(gameRepository.registerNewGame(GameRequest(title), session.accessToken).subscribe({ base->
            viewGame.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data.list as ArrayList<Game>))
        }, {
            viewGame.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
        }))
    }

    fun getSearchGame() = viewSearchGame
    fun getGame() = viewGame

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}