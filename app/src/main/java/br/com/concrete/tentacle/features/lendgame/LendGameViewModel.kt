package br.com.concrete.tentacle.features.lendgame

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository

class LendGameViewModel(private val gameRepository: GameRepository) : BaseViewModel() {
    private val viewState: MutableLiveData<ViewStateModel<Media>> = MutableLiveData()
    fun getMediaViewState() = viewState

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun fetchMediaLoan(id: String) {
        viewState.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(gameRepository.getMediaLoan(id)
            .subscribe({ baseModel ->
                viewState.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data))
            }, {
                viewState.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }
}