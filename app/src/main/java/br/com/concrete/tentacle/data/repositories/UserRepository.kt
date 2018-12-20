package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.network.ApiService
import br.com.concrete.tentacle.features.user.UserViewModelContract
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRepository(private val apiService: ApiService) : UserRepositoryContract {

    override fun registerUser(user: User, callBack: UserViewModelContract) = apiService.registerUser(user)
        .flatMap { baseModel ->
            Observable.just(baseModel)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            callBack.callBackUser(it)
        }, {
            callBack.notKnownError(it)
        }).dispose()

    override fun getCities(stateId: String, callBack: UserViewModelContract) = apiService.getCities(stateId)
        .flatMap { baseModel ->
            Observable.just(baseModel)
        }
        .cache()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            callBack.callBackCities(it)
        }, {
            callBack.notKnownError(it)
        }).dispose()

    override fun getStates(callBack: UserViewModelContract) = apiService.getStates()
        .flatMap { baseModel ->
            Observable.just(baseModel)
        }
        .cache()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            callBack.callBackStates(it)
        }, {
            callBack.notKnownError(it)
        }).dispose()


}