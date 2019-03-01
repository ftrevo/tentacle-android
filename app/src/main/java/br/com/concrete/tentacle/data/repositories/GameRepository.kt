package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.models.LoanActionRequest
import br.com.concrete.tentacle.data.models.LoansListResponse
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class GameRepository(private val apiRest: ApiService) {

    fun getSearchGames(name: String): Observable<BaseModel<GameResponse>> {
        return apiRest.getSearchGames(name)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun loadMyGames(): Observable<BaseModel<MediaResponse>> {
        return apiRest.getRegisteredGames()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun registerNewGame(game: GameRequest): Observable<BaseModel<Game>> {
        return apiRest.registerNewGame(game)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun loadHomeGames(): Observable<BaseModel<GameResponse>> {
        return apiRest.loadHome()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun getMediaLoan(id: String): Observable<BaseModel<Media>> {
        return apiRest.getMediaLoan(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun updateMediaLoan(activeLoanId: String, loanActionRequest: LoanActionRequest): Observable<BaseModel<LoanResponse>> {
        return apiRest.updateMediaLoan(activeLoanId, loanActionRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun loadMyLoans(): Observable<BaseModel<LoansListResponse>> {
        return apiRest.getMyLoans()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun loadMyLoan(loanId: String): Observable<BaseModel<LoanResponse>> {
        return apiRest.getMyLoan(loanId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}