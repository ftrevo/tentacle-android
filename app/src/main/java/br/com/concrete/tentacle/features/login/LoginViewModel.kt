package br.com.concrete.tentacle.features.login

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.AppTentacle
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.data.repositories.TokenRepository
import br.com.concrete.tentacle.utils.Event
import br.com.concrete.tentacle.utils.LogWrapper
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import retrofit2.HttpException
import java.net.HttpURLConnection

class LoginViewModel(
    private val repository: LoginRepository,
    private val sharedPrefRepository: SharedPrefRepository,
    private val tokenRepository: TokenRepository
)
    : BaseViewModel(), LifecycleObserver {

    private val stateModel: MutableLiveData<Event<ViewStateModel<Session>>> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun loginUser(email: String, password: String) {
        stateModel.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(repository.loginUser(email, password).subscribe(
            { base ->
                sharedPrefRepository.saveSession(PREFS_KEY_USER_SESSION, base.data)
                postSession(base.data)
                disposables.add(tokenRepository.sendToken(AppTentacle.TOKEN).subscribe({
                    LogWrapper.log("TokenResponse: ", it.message[0])
                },{
                    LogWrapper.log("TokenResponse: ", it.localizedMessage.toString())
                }))
            },
            {
                stateModel.postValue(Event(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = errorLogin(it))))
            }, {
                LogWrapper.log("LOGIN-USER", "On login complete")
            }
        ))
    }

    fun getStateModel(): LiveData<Event<ViewStateModel<Session>>> = stateModel

    fun isUserLogged() = sharedPrefRepository.getStoredSession(PREFS_KEY_USER_SESSION) != null

    private fun postSession(session: Session) {
        stateModel.postValue(Event(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = session)))
    }

    private fun errorLogin(error: Throwable): ErrorResponse {
        var errorResponse = ErrorResponse()

        if (error is HttpException) {
            if (error.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                errorResponse.messageInt.add(R.string.user_or_password_error)
            } else {
                errorResponse = notKnownError(error)
            }
        } else {
            errorResponse = notKnownError(error)
        }

        return errorResponse
    }
}