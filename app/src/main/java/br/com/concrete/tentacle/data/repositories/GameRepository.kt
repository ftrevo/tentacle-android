package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class GameRepository(private val apiRest: ApiService) {

    fun getSearchGames(title: String, token: String): Observable<BaseModel<GameResponse>> {
        return apiRest.getSearchGames(title, token)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

}