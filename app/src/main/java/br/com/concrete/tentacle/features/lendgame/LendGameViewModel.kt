package br.com.concrete.tentacle.features.lendgame

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.LoanActionRequest
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.RememberDeliveryResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.repositories.GameRepository

class LendGameViewModel(private val gameRepository: GameRepository) : BaseViewModel() {
    private val viewState: MutableLiveData<ViewStateModel<Media>> = MutableLiveData()
    private val viewStateUpdateLoan: MutableLiveData<ViewStateModel<LoanResponse>> = MutableLiveData()
    private val viewStateRememberDelivery: MutableLiveData<ViewStateModel<RememberDeliveryResponse>> = MutableLiveData()

    fun getMediaViewState() = viewState
    fun getUpdateLoanViewState() = viewStateUpdateLoan
    fun getRememberDeliveryViewState() = viewStateRememberDelivery

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

    fun updateMediaLoan(activeLoanId: String, loanActionRequest: LoanActionRequest) {
        viewStateUpdateLoan.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(gameRepository.updateMediaLoan(activeLoanId, loanActionRequest)
            .subscribe({ baseModel ->
                viewStateUpdateLoan.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data))
            }, {
                viewStateUpdateLoan.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }

    fun rememberDelivery() {
        viewStateRememberDelivery.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(gameRepository.rememberDelivery()
            .subscribe({ baseModel ->
                viewStateRememberDelivery.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data))
            }, {
                viewStateRememberDelivery.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }
}