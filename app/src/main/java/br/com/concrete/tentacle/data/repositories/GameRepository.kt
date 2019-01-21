package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class GameRepository(private val apiService: ApiService) {

    fun loadMyGames(): Observable<BaseModel<MediaResponse>> {
        return apiService.getRegisteredGames()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}