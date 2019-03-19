package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.base.AppTentacle
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.RequestLogin
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.network.ApiServiceAuthentication
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class LoginRepository(private val apiRestAuthentication: ApiServiceAuthentication) {

    fun loginUser(email: String, password: String): Flowable<BaseModel<Session>> {
        val token = AppTentacle.TOKEN ?: null
        return apiRestAuthentication.loginUser(RequestLogin(email, password, token))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}