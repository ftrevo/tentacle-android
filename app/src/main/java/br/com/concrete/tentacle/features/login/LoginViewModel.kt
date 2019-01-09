package br.com.concrete.tentacle.features.login

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.repositories.LoginRepository
import br.com.concrete.tentacle.repositories.SharedPrefRepository
import br.com.concrete.tentacle.utils.LogWrapper
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import retrofit2.HttpException
import java.net.HttpURLConnection

class LoginViewModel(
    private val repository: LoginRepository,
    private val sharedPrefRepository: SharedPrefRepository
)
    : BaseViewModel(), LifecycleObserver {

    private val stateModel: MutableLiveData<ViewStateModel<Session>> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun loginUser(email: String, password: String) {
        stateModel.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        repository.loginUser(email, password).subscribe(
            { base ->
                sharedPrefRepository.saveSession(PREFS_KEY_USER_SESSION, base.data)
                stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data))
            },
            {
                stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = errorLogin(it)))
            }, {
                LogWrapper.log("LOGIN-USER", "On login complete")
            }
        )
    }

    fun getStateModel(): LiveData<ViewStateModel<Session>> = stateModel

    private fun errorLogin(error: Throwable): ErrorResponse {
        var errorResponse = ErrorResponse()

        if (error.cause is HttpException && (error.cause as HttpException).code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            errorResponse.messageInt.add(R.string.user_or_password_error)
        } else {
            errorResponse = notKnownError(error)
        }

        return errorResponse
    }
}