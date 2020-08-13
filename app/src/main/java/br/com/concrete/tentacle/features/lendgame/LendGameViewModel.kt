package br.com.concrete.tentacle.features.lendgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ActiveMedia
import br.com.concrete.tentacle.data.models.LoanActionRequest
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.RememberDeliveryResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.SingleEvent

class LendGameViewModel(private val gameRepository: GameRepository) : BaseViewModel() {
    private val viewState: MutableLiveData<ViewStateModel<Media>> = MutableLiveData()
    private val viewStateUpdateLoan: MutableLiveData<ViewStateModel<LoanResponse>> = MutableLiveData()
    private val viewStateRememberDelivery: MutableLiveData<ViewStateModel<RememberDeliveryResponse>> = MutableLiveData()
    private val viewStateGameDelete: MutableLiveData<SingleEvent<ViewStateModel<Media>>> = MutableLiveData()
    private val viewStateGameActive: MutableLiveData<SingleEvent<ViewStateModel<Media>>> = MutableLiveData()

    fun deleteMedia(): LiveData<SingleEvent<ViewStateModel<Media>>> = viewStateGameDelete
    fun getMediaViewState() = viewState
    fun getUpdateLoanViewState() = viewStateUpdateLoan
    fun getRememberDeliveryViewState() = viewStateRememberDelivery
    fun activeMediaState(): LiveData<SingleEvent<ViewStateModel<Media>>> = viewStateGameActive

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
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                }, {
                    viewStateGameDelete.postValue(
                        SingleEvent(
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
        val activeMedia = ActiveMedia(active)

        disposables.add(
            gameRepository.activeMedia(media._id, activeMedia)
                .subscribe({ baseModel ->
                    viewStateGameActive.postValue(
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                }, {
                    viewStateGameActive.postValue(
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.ERROR,
                                errors = notKnownError(it)
                            )
                        )
                    )
                })
        )
    }
}