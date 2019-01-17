package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.CityResponse
import br.com.concrete.tentacle.data.models.StateResponse
import br.com.concrete.tentacle.data.network.ApiServiceAuthentication
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class UserRepository(private val apiServiceAuthentication: ApiServiceAuthentication) {

    fun registerUser(userRequest: UserRequest): Observable<BaseModel<User>> {

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
}
