package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.network.ApiService
import br.com.concrete.tentacle.data.network.ApiServiceWithToken
import io.reactivex.Observable


class GameRepository(apiServiceWithToken: ApiServiceWithToken){

    fun loadMyGames(): Observable<BaseModel<MediaResponse>>?{
        return null
    }

}
