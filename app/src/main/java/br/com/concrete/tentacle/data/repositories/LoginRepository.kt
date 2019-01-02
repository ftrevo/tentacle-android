package br.com.concrete.tentacle.data.repositories

import android.util.Log
import br.com.concrete.tentacle.data.models.RequestLogin
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class LoginRepository(private val apiRest: ApiService) {

    private val disposableList: CompositeDisposable = CompositeDisposable()

    fun loginUser(email: String, password: String,
                  onSuccess: (Session, List<String>) -> Unit,
                  onError: (String) -> Unit) {
        val disposable = apiRest.loginUser(RequestLogin(email, password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    onSuccess(response.data, response.message)
                },
                {
                    onError((it as HttpException).response().errorBody()?.string()!!)
                },
                {
                    Log.d("LOGIN-USER", "On login complete")
                }
            )
        disposableList.add(disposable)
    }
}