package br.com.concrete.tentacle.features.loadmygames

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.Event

class LoadMyGamesViewModel(private val gameRepository: GameRepository) : BaseViewModel() {

    private val viewStateGame: MutableLiveData<Event<ViewStateModel<MediaResponse>>> = MutableLiveData()
    fun getMyGames(): LiveData<Event<ViewStateModel<MediaResponse>>> = viewStateGame

    var page: Int = 1

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun loadMyGames() {
        viewStateGame.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(gameRepository.loadMyGames()
            .subscribe({ baseModel ->
                viewStateGame.postValue(Event(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data)))
            }, {
                viewStateGame.postValue(Event(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it))))
            })
        )
    }

    fun loadGamePage() {
        disposables.add(gameRepository.loadMyGames(page)
            .subscribe({ baseModel ->
                viewStateGame.postValue(Event(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data)))
                page += 1
            }, {
                viewStateGame.postValue(Event(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it))))
                page -= 1
            })
        )
    }

}
