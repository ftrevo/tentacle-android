package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.CityResponse
import br.com.concrete.tentacle.data.models.StateResponse
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class UserRepository(private val apiService: ApiService): UserRepositoryContract {
    override fun registerUser(user: User, success: (BaseModel<User>) -> Unit, error: (Throwable) -> Unit) {
        val disposable = apiService.registerUser(user)
            .flatMap { baseModel ->
                Observable.just(baseModel)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                success(it)
            }, {
                kotlin.error(it);
            })

        this.disposables.add(disposable)
    }

    override fun getCities(stateId: String, success: (BaseModel<CityResponse>) -> Unit, error: (Throwable) -> Unit) {
        val disposable = apiService.getCities(stateId)
            .flatMap { baseModel ->
                Observable.just(baseModel)
            }
            .cache()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                success(it)
            }, {
                error(it)
            })

        this.disposables.add(disposable)
    }

    override fun getStates(success: (BaseModel<StateResponse>) -> Unit, error: (Throwable) -> Unit) {
        val disposable = apiService.getStates()
            .flatMap { baseModel ->
                Observable.just(baseModel)
            }
            .cache()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                success(it)
            }, {
                error(it)
            })

        this.disposables.add(disposable)
    }

    private var disposables: ArrayList<Disposable> = ArrayList<Disposable>()


    override fun disposeAll() {
        this.disposables.forEach{disposable ->
            disposable.dispose()
        }
    }


}

