package br.com.concrete.tentacle.features.login

import androidx.lifecycle.*
import br.com.concrete.tentacle.extensions.fromJson
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import com.google.gson.Gson


class LoginViewModel(private val repository: LoginRepository,private val sharedPrefRepository: SharedPrefRepository):
    ViewModel(), LifecycleObserver {

    private val stateModel: MutableLiveData<ViewStateModel<Session>> = MutableLiveData()

    fun loginUser(email: String, password: String) {
        stateModel.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        repository.loginUser(email, password,
            { session, messages ->
                sharedPrefRepository.saveSession("USER_SESSION", session)
                stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = session))
            },
            {
                val errors = Gson().fromJson<ErrorResponse>(it)
                stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = errors))
            }
        )
    }

    fun getStateModel(): LiveData<ViewStateModel<Session>> = stateModel

}