package br.com.concrete.tentacle.features.forgotPassword.passwordRecovery

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.PasswordRecovery
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract
import br.com.concrete.tentacle.data.repositories.UserRepository

class PasswordRecoveryViewModel(
    private val userRepository: UserRepository,
    private val sharedPrefRepository: SharedPrefRepositoryContract
) : BaseViewModel() {

    val stateModel: MutableLiveData<ViewStateModel<Session>> = MutableLiveData()

    fun restorePassword(passwordRecovery: PasswordRecovery) {
        stateModel.postValue(ViewStateModel(ViewStateModel.Status.LOADING))

        disposables.add(
            userRepository.restorePassword(passwordRecovery)
                .subscribe(
                    { baseSession ->
                        sharedPrefRepository.saveSession(baseSession.data)
                        stateModel.postValue(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseSession.data))
                    },
                    {
                        stateModel.postValue(
                            ViewStateModel(
                                status = ViewStateModel.Status.ERROR,
                                errors = notKnownError(it)))
                    }
                )
        )
    }
}