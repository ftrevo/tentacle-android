package br.com.concrete.tentacle.data.repositories

import android.util.Log
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class LoginRepository(private val apiRest: ApiService) {

    fun loginUser(email: String, password: String,
                  onSuccess: (Session, List<String>) -> Unit,
                  onError: (Throwable) -> Unit) {
        apiRest.loginUser(email, password)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    onSuccess(response.data, response.message)
                },
                {
                    onError(it)
                },
                {
                    Log.d("LOGIN-USER", "On login complete")
                }
            ).dispose()

    }

}
