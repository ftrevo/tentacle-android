package br.com.concrete.tentacle.features.lendgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.enums.Platform
import br.com.concrete.tentacle.data.models.LoanActionRequest
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaRequest
import br.com.concrete.tentacle.data.models.RememberDeliveryResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.Event
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_3DS
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_3DS_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_SWITCH
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_SWITCH_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_360
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_360_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_ONE
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_ONE_ABBREV

class LendGameViewModel(private val gameRepository: GameRepository) : BaseViewModel() {
    private val viewState: MutableLiveData<ViewStateModel<Media>> = MutableLiveData()
    private val viewStateUpdateLoan: MutableLiveData<ViewStateModel<LoanResponse>> = MutableLiveData()
    private val viewStateRememberDelivery: MutableLiveData<ViewStateModel<RememberDeliveryResponse>> = MutableLiveData()
    private val viewStateGameDelete: MutableLiveData<Event<ViewStateModel<Media>>> = MutableLiveData()
    private val viewStateGameActive: MutableLiveData<Event<ViewStateModel<Media>>> = MutableLiveData()

    fun deleteMedia(): LiveData<Event<ViewStateModel<Media>>> = viewStateGameDelete
    fun getMediaViewState() = viewState
    fun getUpdateLoanViewState() = viewStateUpdateLoan
    fun getRememberDeliveryViewState() = viewStateRememberDelivery
    fun activeMediaState(): LiveData<Event<ViewStateModel<Media>>> = viewStateGameActive

    fun fetchMediaLoan(id: String) {
        viewState.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            gameRepository.getMediaLoan(id)
                .subscribe({ baseModel ->
                    viewState.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data))
                }, {
                    viewState.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                })
        )
    }

    fun updateMediaLoan(activeLoanId: String, loanActionRequest: LoanActionRequest) {
        viewStateUpdateLoan.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            gameRepository.updateMediaLoan(activeLoanId, loanActionRequest)
                .subscribe({ baseModel ->
                    viewStateUpdateLoan.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = baseModel.data
                        )
                    )
                }, {
                    viewStateUpdateLoan.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                })
        )
    }

    fun rememberDelivery(id: String?) {
        viewStateRememberDelivery.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            gameRepository.rememberDelivery(id)
                .subscribe({ baseModel ->
                    viewStateRememberDelivery.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = baseModel.data
                        )
                    )
                }, {
                    viewStateRememberDelivery.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                })
        )
    }

    fun deleteGame(id: String) {
        disposables.add(
            gameRepository.deleteMedia(id)
                .subscribe({ baseModel ->
                    viewStateGameDelete.postValue(
                        Event(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                }, {
                    viewStateGameDelete.postValue(
                        Event(
                            ViewStateModel(
                                status = ViewStateModel.Status.ERROR,
                                errors = notKnownError(it)
                            )
                        )
                    )
                })
        )
    }

    fun activeMedia(media: Media, active: Boolean) {
        val mediaRequest = MediaRequest(getPlatform(media.platform), media.game?._id!!)

        disposables.add(
            gameRepository.activeMedia(media._id, mediaRequest, active)
                .subscribe({ baseModel ->
                    viewStateGameActive.postValue(
                        Event(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                }, {
                    viewStateGameActive.postValue(
                        Event(
                            ViewStateModel(
                                status = ViewStateModel.Status.ERROR,
                                errors = notKnownError(it)
                            )
                        )
                    )
                })
        )
    }

    private fun getPlatform(platform: Platform): String {
        return when (platform.platformName) {
            PLATFORM_XBOX_ONE_ABBREV -> PLATFORM_XBOX_ONE
            PLATFORM_XBOX_360_ABBREV -> PLATFORM_XBOX_360
            PLATFORM_NINTENDO_SWITCH_ABBREV -> PLATFORM_NINTENDO_SWITCH
            PLATFORM_NINTENDO_3DS_ABBREV -> PLATFORM_NINTENDO_3DS
            else -> {
                ""
            }
        }
    }
}