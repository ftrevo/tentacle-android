package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.RequestLogin
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginRepository(private val apiRest: ApiService) {

    fun loginUser(email: String, password: String): Flowable<BaseModel<Session>> {
        return apiRest.loginUser(RequestLogin(email, password))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}