package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class GameRepository(private val apiRest: ApiService) {

    fun getSearchGames(title: String, token: String): Observable<BaseModel<GameResponse>> {
        return apiRest.getSearchGames(title, "JWT $token")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun registerNewGame(game: GameRequest, token: String): Observable<BaseModel<Game>> {
        return apiRest.registerNewGame(game, "JWT $token")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}