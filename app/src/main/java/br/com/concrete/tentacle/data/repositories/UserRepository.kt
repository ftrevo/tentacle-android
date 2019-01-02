package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.*
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserRepository(private val apiService: ApiService): UserRepositoryContract {
    override fun registerUser(userRequest: UserRequest, success: (BaseModel<User>) -> Unit, error: (Throwable) -> Unit) {
        val disposable = apiService.registerUser(userRequest)
            .flatMap { baseModel ->
                Observable.just(baseModel)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                success(it)
            }, {
                error(it)
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

    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun disposeAll() {
        disposables.clear()
    }
}

