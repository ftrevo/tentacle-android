package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.*
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class GameRepository(private val apiRest: ApiService) {

    fun getSearchGames(name: String, page: Int = 0): Observable<BaseModel<GameResponse>> {
        return apiRest.getSearchGames(name = name, page = page)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun loadMyGames(queries: QueryParameters = QueryParameters()): Observable<BaseModel<MediaResponse>> {
        return apiRest.getRegisteredGames(
            page = queries.page,
            active = queries.active)
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

    fun loadRemoteGames(gameName: String, page: Int = 0): Observable<BaseModel<GameResponse>> {
        return apiRest.loadRemoteGames(gameName = gameName, page = page)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun registerRemoteGame(game: GameRequest): Observable<BaseModel<Game>> {
        return apiRest.registerRemoteGame(game)
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

    fun getDetailsGame(idGame: String): Observable<BaseModel<Game>> {
        return apiRest.getDetailsGame(idGame)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun loadMyLoans(page: Int = 0): Observable<BaseModel<LoansListResponse>> {
        return apiRest.getMyLoans(page = page)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun loadMyLoan(loanId: String): Observable<BaseModel<LoanResponse>> {
        return apiRest.getMyLoan(loanId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun rememberDelivery(id: String?): Observable<BaseModel<RememberDeliveryResponse>> {
        return apiRest.rememberDelivery(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun deleteMedia(mediaId: String): Observable<BaseModel<Media>> {
        return apiRest.deleteMedia(mediaId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}