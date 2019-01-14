package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.*
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class GameRepository(private val apiRest: ApiService) {

    fun getRegisteredGames(mineOnly: Boolean, token: String): Observable<BaseModel<MediaResponse>> {
        return apiRest.getRegisteredGames(mineOnly, "JWT $token")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun getSearchGames(title: String, token: String): Observable<BaseModel<GameResponse>> {
        return apiRest.getSearchGames(title, "JWT $token")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun registerNewGame(game: GameRequest, token: String): Observable<BaseModel<GameResponse>> {
        return apiRest.registerNewGame(game, "JWT $token")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}