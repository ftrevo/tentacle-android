package br.com.concrete.tentacle.features.registerMedia

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.MediaRequest
import br.com.concrete.tentacle.data.models.StatusLiveData
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.RegisterMediaRepository
import br.com.concrete.tentacle.utils.LogWrapper
import io.reactivex.disposables.CompositeDisposable

class RegisterMediaViewModel(private val repository: RegisterMediaRepository) : BaseViewModel() {

    val viewStatusModel: StatusLiveData = StatusLiveData()

    fun registerMedia(platform: String, game: Game) {
        viewStatusModel.postValue(ViewStateModel(ViewStateModel.Status.LOADING))

        val mediaRequest = MediaRequest(platform, game._id)

        disposables.add(
            repository.registerMedia(mediaRequest)
                .subscribe({
                    viewStatusModel.postValue(ViewStateModel(ViewStateModel.Status.SUCCESS))
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