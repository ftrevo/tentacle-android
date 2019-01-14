package br.com.concrete.tentacle.features.game

import androidx.lifecycle.*
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.extensions.fromJson
import br.com.concrete.tentacle.utils.LogWrapper
import br.com.concrete.tentacle.utils.PREFS_KEY_USER
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable

class MyGameViewModel(
    private val gameRepository: GameRepository,
    private val sharedPrefRepository: SharedPrefRepository
)
    : BaseViewModel(), LifecycleObserver {

    private val disposables = CompositeDisposable()
    private val liveGame = MutableLiveData<ViewStateModel<ArrayList<Media>>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun getRegisteredGames() {
        liveGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        //val user: User = Gson().fromJson(sharedPrefRepository.getStoreString(PREFS_KEY_USER)!!)
        val session: Session = Gson().fromJson(sharedPrefRepository.getStoreString(PREFS_KEY_USER_SESSION)!!)
        disposables.add(gameRepository.getRegisteredGames(true, session.accessToken).subscribe({ base->
            liveGame.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data.list as ArrayList<Media>))
        }, {
            liveGame.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
        }, {
            LogWrapper.log("GAME-REGISTERED", "GAME REGISTERED")
        }))
    }

    fun getLiveGame(): LiveData<ViewStateModel<ArrayList<Media>>> = liveGame

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}