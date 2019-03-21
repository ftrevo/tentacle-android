package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class UserLoggedRepository(private val api: ApiService) {

    fun getProfile(): Observable<BaseModel<User>> {
        return api.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun updateProfile(userId: String, userRequest: UserRequest): Observable<BaseModel<Session>> {
        return api.updateUserProfile(userId, userRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}
