package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.ActiveMedia
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.models.LoanActionRequest
import br.com.concrete.tentacle.data.models.LoansListResponse
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.RememberDeliveryResponse
import br.com.concrete.tentacle.data.models.library.loan.LoanDeleteResponse
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

    fun loadMyGames(query: QueryParameters = QueryParameters()): Observable<BaseModel<MediaResponse>> {
        return apiRest.getRegisteredGames(
            page = query.page,
            active = query.active)
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

    fun loadMyLoans(page: Int = 0, queries: QueryParameters): Observable<BaseModel<LoansListResponse>> {
        return apiRest.getMyLoans(page = page, showHistory = queries.showHistory)
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

    fun deleteLoan(idLoan: String): Observable<BaseModel<LoanDeleteResponse>> {
        return apiRest.deleteLoan(idLoan)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun activeMedia(idMedia: String, activeMedia: ActiveMedia): Observable<BaseModel<Media>> {
        return apiRest.activeMedia(idMedia, activeMedia)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}