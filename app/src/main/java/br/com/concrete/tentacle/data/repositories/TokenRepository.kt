package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.MessageReturn
import br.com.concrete.tentacle.data.models.RequestUpdateToken
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class TokenRepository(private val apiService: ApiService) {

    fun sendToken(token: String?): Observable<BaseModel<MessageReturn>> {
        return apiService.sendToken(RequestUpdateToken(token))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

}