package br.com.concrete.tentacle.features.myreservations

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.LoansListResponse
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanDeleteResponse
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.SingleEvent

class MyReservationViewModel(private val gameRepository: GameRepository) : BaseViewModel() {

    private val viewStateGame: MutableLiveData<ViewStateModel<LoansListResponse>> = MutableLiveData()
    private val viewStateGamePage: MutableLiveData<SingleEvent<ViewStateModel<LoansListResponse>>> = MutableLiveData()
    fun getMyReservations() = viewStateGame
    fun getMyReservationsPage() = viewStateGamePage
    private val viewStateDeleteLoan: MutableLiveData<ViewStateModel<LoanDeleteResponse>> = MutableLiveData()
    fun getStateDeleteLoan(): LiveData<ViewStateModel<LoanDeleteResponse>> = viewStateDeleteLoan

    var page: Int = 1

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadMyReservations() {
        myReservations()
    }

    fun myReservations(queryParameters: QueryParameters? = null) {
        val queries = queryParameters ?: QueryParameters()

        viewStateGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            getDisposable(queries)
        )
    }

    private fun getDisposable(queryParameters: QueryParameters) = gameRepository.loadMyLoans(queries = queryParameters)
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

    fun loadMyReservationsPage(queryParameters: QueryParameters?) {
        val queries = queryParameters ?: QueryParameters()
        disposables.add(
            getDisposablePage(queries)
        )
    }

    private fun getDisposablePage(queryParameters: QueryParameters) =
        gameRepository.loadMyLoans(page, queries = queryParameters)
            .subscribe({ baseModel ->
                viewStateGamePage.postValue(
                    SingleEvent(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = baseModel.data
                        )
                    )
                )
                page += 1
            }, {
                viewStateGamePage.postValue(
                    SingleEvent(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                )
            })

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

    fun resetPage() {
        this.page = 1
    }
}
