package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.network.ApiService
import br.com.concrete.tentacle.testing.OpenForTesting
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

@OpenForTesting
class GameRepository(private val apiRest: ApiService) {

    fun getSearchGames(title: String): Observable<BaseModel<GameResponse>> {
        return apiRest.getSearchGames(title)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun loadMyGames(){

    }

    fun registerNewGame(game: GameRequest): Observable<BaseModel<Game>> {
        return apiRest.registerNewGame(game)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}