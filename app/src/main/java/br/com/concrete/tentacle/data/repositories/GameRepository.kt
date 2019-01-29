package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class GameRepository(private val apiRest: ApiService) {

    fun getSearchGames(title: String): Observable<BaseModel<GameResponse>> {
        return apiRest.getSearchGames(title)
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

    fun getLibrary(): Observable<BaseModel<LibraryResponse>> {
        return apiRest.getLibrary()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}