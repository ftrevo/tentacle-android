package br.com.concrete.tentacle.features.login

import android.annotation.SuppressLint
import androidx.lifecycle.*
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import br.com.concrete.tentacle.utils.LogWrapper
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

    fun isUserLogged() = sharedPrefRepository.getStoredSession(PREFS_KEY_USER_SESSION) != null

    private fun sendSession(session: Session){
        stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = session))
    }

    private fun errorLogin(error: Throwable): ErrorResponse {
        var errorResponse = ErrorResponse()

        if (error is HttpException && error.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            errorResponse.messageInt.add(R.string.user_or_password_error)
        } else {
            errorResponse = super.notKnownError(error)
        }

        return errorResponse
    }
}