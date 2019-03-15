package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.UserRepository

class ForgotPassSendEmailViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    private val stateModel = MutableLiveData<ViewStateModel<User>>()
    fun getStateModel(): LiveData<ViewStateModel<User>> = stateModel

    fun forgotPassword(email: String) {
        stateModel.postValue(ViewStateModel(ViewStateModel.Status.LOADING))

        disposables.add(
            userRepository.forgotPassword(email)
                .subscribe({ baseModel ->
                    stateModel.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = baseModel.data
                        )
                    )
                }, {
                    stateModel.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                })
        )
    }
}