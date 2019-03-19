package br.com.concrete.tentacle.features.myreservations

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.LoansListResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.Event

class MyReservationViewModel(private val gameRepository: GameRepository) : BaseViewModel() {

    private val viewStateGame: MutableLiveData<ViewStateModel<LoansListResponse>> = MutableLiveData()
    private val viewStateGamePage: MutableLiveData<Event<ViewStateModel<LoansListResponse>>> = MutableLiveData()
    fun getMyReservations() = viewStateGame
    fun getMyReservationsPage() = viewStateGamePage

    var page: Int = 1

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadMyReservations() {
        viewStateGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(gameRepository.loadMyLoans()
            .subscribe({ baseModel ->
                viewStateGame.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data))
            }, {
                viewStateGame.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }

    fun loadMyReservationsPage() {
        disposables.add(gameRepository.loadMyLoans(page)
            .subscribe({ baseModel ->
                viewStateGamePage.postValue(Event(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data)))
                page+= 1
            }, {
                viewStateGamePage.postValue(Event(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it))))
            })
        )
    }
}
