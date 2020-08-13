package br.com.concrete.tentacle.features.myreservations.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanDeleteResponse
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.repositories.GameRepository

class MyReservationDetailViewModel(private val gameRepository: GameRepository) : BaseViewModel() {
    private val viewStateGame: MutableLiveData<ViewStateModel<LoanResponse>> = MutableLiveData()
    fun getViewState() = viewStateGame

    private val viewStateDeleteLoan: MutableLiveData<ViewStateModel<LoanDeleteResponse>> = MutableLiveData()
    fun getStateDeleteLoan(): LiveData<ViewStateModel<LoanDeleteResponse>> = viewStateDeleteLoan

    fun loadMyLoan(loanId: String) {
        viewStateGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            gameRepository.loadMyLoan(loanId)
                .subscribe({ baseModel ->
                    viewStateGame.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = baseModel.data
                        )
                    )
                }, {
                    viewStateGame.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                })
        )
    }

    fun deleteLoan(idLoan: String) {
        viewStateDeleteLoan.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            gameRepository.deleteLoan(idLoan)
                .subscribe({ baseModel ->
                    viewStateDeleteLoan.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = baseModel.data
                        )
                    )
                }, {
                    viewStateDeleteLoan.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                })
        )
    }
}