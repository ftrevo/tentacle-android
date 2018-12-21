package br.com.concrete.tentacle.features.login

import androidx.lifecycle.*
import br.com.concrete.tentacle.base.fromJson
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.LoginRepository
import com.google.gson.Gson


class LoginViewModel(private val repository: LoginRepository): ViewModel(), LifecycleObserver {

    private val stateModel: MutableLiveData<ViewStateModel<Session>> = MutableLiveData()

    fun loginUser(email: String, password: String) {
        if (stateModel.value == null) {
            stateModel.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
            repository.loginUser(email, password,
                { session, messages ->
                    stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = session))
                },
                {
                    val errors = Gson().fromJson<List<String>>(it)
                    stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = errors))
                }
            )
        }
    }

    fun getStateModel(): LiveData<ViewStateModel<Session>> = stateModel

}