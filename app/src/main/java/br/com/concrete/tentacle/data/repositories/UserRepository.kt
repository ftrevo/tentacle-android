package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserRepository(private val apiService: ApiService) : UserRepositoryContract {

    override fun registerUser(user: User) = apiService.registerUser(user)
        .flatMap { baseModel ->
            Observable.just(baseModel)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({

        }, {

        }, {

        }).dispose()

    override fun getCities(state_id: String) = apiService.getCities(state_id)
        .flatMap { baseModel ->
            Observable.just(baseModel)
        }
        .cache()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({

        }, {

        }, {

        }).dispose()

    override fun getStates() = apiService.getStates()
        .flatMap { baseModel ->
            Observable.just(baseModel)
        }
        .cache()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({

        }, {

        }, {

        }).dispose()

}