package br.com.concrete.tentacle.features.myreservations.detail

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.features.myreservations.MyReservationBaseViewModel

class MyReservationDetailViewModel(private val gameRepository: GameRepository) : MyReservationBaseViewModel() {
    private val viewStateGame: MutableLiveData<ViewStateModel<LoanResponse>> = MutableLiveData()
    fun getViewState() = viewStateGame

    fun loadMyLoan(loanId: String) {
        viewStateGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(gameRepository.loadMyLoan(loanId)
            .subscribe({ baseModel ->
                viewStateGame.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data))
            }, {
                viewStateGame.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }
}