package br.com.concrete.tentacle.features.registerGame.registerMedia

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.data.repositories.RegisterMediaRepository
import br.com.concrete.tentacle.utils.SingleEvent
import br.com.concrete.tentacle.utils.LogWrapper

class RegisterMediaViewModel(
    private val repository: RegisterMediaRepository,
    private val gameRepository: GameRepository
) : BaseViewModel() {

    val viewStatusModel = MutableLiveData<SingleEvent<ViewStateModel<Media>>>()
    private val viewStateModelGame = MutableLiveData<SingleEvent<ViewStateModel<Game>>>()

    fun registerMedia(platform: String, game: Game) {
        viewStatusModel.postValue(SingleEvent(ViewStateModel(ViewStateModel.Status.LOADING)))

        val mediaRequest = MediaRequest(platform, game._id)

        disposables.add(
            repository.registerMedia(mediaRequest)
                .subscribe({
                    viewStatusModel.postValue(
                        SingleEvent(
                            ViewStateModel(
                                ViewStateModel.Status.SUCCESS, it.data
                            )
                        )
                    )
                },
                    {
                        viewStatusModel.postValue(
                            SingleEvent(
                                ViewStateModel(
                                    status = ViewStateModel.Status.ERROR,
                                    errors = notKnownError(it)
                                )
                            )
                        )
                        LogWrapper.print(it)
                    })
        )
    }

    fun getRegisterMedia(): LiveData<SingleEvent<ViewStateModel<Media>>> = viewStatusModel

    fun getDetailsGame(idGame: String) {
        viewStateModelGame.postValue(SingleEvent(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(
            gameRepository.getDetailsGame(idGame)
                .subscribe({
                    viewStateModelGame.postValue(
                        SingleEvent(
                            ViewStateModel(
                                ViewStateModel.Status.SUCCESS, it.data
                            )
                        )
                    )
                },
                    {
                        viewStateModelGame.postValue(
                            SingleEvent(
                                ViewStateModel(
                                    status = ViewStateModel.Status.ERROR,
                                    errors = notKnownError(it)
                                )
                            )
                        )
                        LogWrapper.print(it)
                    })
        )
    }

    fun getDetailGame(): LiveData<SingleEvent<ViewStateModel<Game>>> = viewStateModelGame

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}