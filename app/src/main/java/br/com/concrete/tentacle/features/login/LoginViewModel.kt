package br.com.concrete.tentacle.features.login

import androidx.lifecycle.*
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.LoginRepository

class LoginViewModel(private val repository: LoginRepository): ViewModel(), LifecycleObserver {

    val stateModel: MutableLiveData<ViewStateModel<Session>> = MutableLiveData()

    fun loginUser(email: String, password: String) {
        if (stateModel.value == null) {
            stateModel.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
            repository.loginUser(email, password,
                { session, messages ->
                    stateModel.postValue(ViewStateModel(ViewStateModel.Status.SUCCESS, session))
                },
                {
                    stateModel.postValue(ViewStateModel(ViewStateModel.Status.ERROR))
                }
            )
        }
    }

}