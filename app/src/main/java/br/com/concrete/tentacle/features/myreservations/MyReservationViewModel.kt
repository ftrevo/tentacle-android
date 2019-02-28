package br.com.concrete.tentacle.features.myreservations

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.repositories.GameRepository

class MyReservationViewModel(private val gameRepository: GameRepository) : BaseViewModel() {

    private val viewStateGame: MutableLiveData<ViewStateModel<ArrayList<LoanResponse>>> = MutableLiveData()
    fun getMyReservations() = viewStateGame

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadMyReservations() {
        viewStateGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(gameRepository.loadMyLoans()
            .subscribe({ baseModel ->
                viewStateGame.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data.list as ArrayList<LoanResponse>))
            }, {
                viewStateGame.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }
}
