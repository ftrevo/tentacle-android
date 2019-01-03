package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.*
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRepository(private val apiService: ApiService){

    fun registerUser(userRequest: UserRequest): Observable<BaseModel<User>> {

        return apiService.registerUser(userRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCities(stateId: String): Observable<BaseModel<CityResponse>> {
        return apiService.getCities(stateId)
            .cache()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getStates(): Observable<BaseModel<StateResponse>> {
        return apiService.getStates()
            .cache()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

