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
import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract
import br.com.concrete.tentacle.data.repositories.TokenRepository
import br.com.concrete.tentacle.utils.SingleEvent
import br.com.concrete.tentacle.utils.LogWrapper
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import retrofit2.HttpException
import java.net.HttpURLConnection

class LoginViewModel(
    private val repository: LoginRepository,
    private val sharedPrefRepository: SharedPrefRepositoryContract,
    private val tokenRepository: TokenRepository
)
    : BaseViewModel(), LifecycleObserver {

    private val stateModel: MutableLiveData<SingleEvent<ViewStateModel<Session>>> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun loginUser(email: String, password: String) {
        stateModel.postValue(SingleEvent(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(repository.loginUser(email, password).subscribe(
            { base ->
                sharedPrefRepository.saveSession(base.data)
                postSession(base.data)
                disposables.add(tokenRepository.sendToken(AppTentacle.TOKEN).subscribe({
                    LogWrapper.log("TokenResponse: ", it.message[0])
                }, {
                    LogWrapper.log("TokenResponse: ", it.localizedMessage.toString())
                }))
            },
            {
                stateModel.postValue(SingleEvent(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = errorLogin(it))))
            }, {
                LogWrapper.log("LOGIN-USER", "On login complete")
            }
        ))
    }

    fun getStateModel(): LiveData<SingleEvent<ViewStateModel<Session>>> = stateModel

    fun isUserLogged() = sharedPrefRepository.getStoredSession(PREFS_KEY_USER_SESSION) != null

    private fun postSession(session: Session) {
        stateModel.postValue(SingleEvent(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = session)))
    }

    private fun errorLogin(error: Throwable): ErrorResponse? {
        var errorResponse: ErrorResponse? = null

        if (error is HttpException) {
            if (error.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                val msgsInt = ArrayList<Int>()
                msgsInt.add(R.string.user_or_password_error)

                errorResponse = ErrorResponse(messageInt = msgsInt, statusCode = error.code())
            } else {
                errorResponse = notKnownError(error)
            }
        } else {
            errorResponse = notKnownError(error)
        }

        return errorResponse
    }
}