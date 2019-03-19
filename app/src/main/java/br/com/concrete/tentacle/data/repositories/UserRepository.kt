package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.CityResponse
import br.com.concrete.tentacle.data.models.ForgotPassword
import br.com.concrete.tentacle.data.models.PasswordRecovery
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.StateResponse
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.network.ApiServiceAuthentication
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class UserRepository(private val apiServiceAuthentication: ApiServiceAuthentication) {

    fun registerUser(userRequest: UserRequest): Observable<BaseModel<Session>> {
        return apiServiceAuthentication.registerUser(userRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun getCities(stateId: String): Observable<BaseModel<CityResponse>> {
        return apiServiceAuthentication.getCities(stateId)
            .cache()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun getStates(): Observable<BaseModel<StateResponse>> {
        return apiServiceAuthentication.getStates()
            .cache()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun forgotPassword(email: String): Observable<BaseModel<User>> {
        return apiServiceAuthentication.forgotPassword(ForgotPassword(email))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun restorePassword(passwordRecovery: PasswordRecovery): Flowable<BaseModel<Session>> {
        return apiServiceAuthentication.restorePassword(passwordRecovery)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}
