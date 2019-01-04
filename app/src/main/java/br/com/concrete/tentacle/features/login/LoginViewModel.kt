package br.com.concrete.tentacle.features.login

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.utils.LogWrapper
import retrofit2.HttpException

class LoginViewModel(private val repository: LoginRepository, private val sharedPrefRepository: SharedPrefRepository) :
    BaseViewModel(), LifecycleObserver {

    private val stateModel: MutableLiveData<ViewStateModel<Session>> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun loginUser(email: String, password: String) {
        stateModel.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        repository.loginUser(email, password).subscribe(
            { base ->
                sharedPrefRepository.saveSession("USER_SESSION", base.data)
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

        if (error is HttpException && error.code() == 401) {
            errorResponse.message.add("Usuário ou senha inválidos!")
        } else {
            errorResponse = super.notKnownError(error)
        }

        return errorResponse
    }
}