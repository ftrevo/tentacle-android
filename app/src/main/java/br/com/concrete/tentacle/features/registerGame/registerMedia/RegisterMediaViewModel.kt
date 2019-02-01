package br.com.concrete.tentacle.features.registerGame.registerMedia

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.MediaRequest
import br.com.concrete.tentacle.data.models.RegisteredMediaResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.RegisterMediaRepository
import br.com.concrete.tentacle.utils.LogWrapper

class RegisterMediaViewModel(private val repository: RegisterMediaRepository) : BaseViewModel() {

    val viewStatusModel = MutableLiveData<ViewStateModel<RegisteredMediaResponse>>()

    fun registerMedia(platform: String, game: Game) {
        viewStatusModel.postValue(ViewStateModel(ViewStateModel.Status.LOADING))

        val mediaRequest = MediaRequest(platform, game._id)

        disposables.add(
            repository.registerMedia(mediaRequest)
                .subscribe({
                    viewStatusModel.postValue(ViewStateModel(ViewStateModel.Status.SUCCESS, it.data))
                },
                {
                    viewStatusModel.postValue(ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it)))
                    LogWrapper.print(it)
                })
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}