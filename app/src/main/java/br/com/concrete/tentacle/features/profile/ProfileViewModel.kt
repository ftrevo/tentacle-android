package br.com.concrete.tentacle.features.profile

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.data.repositories.UserLoggedRepository
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION

class ProfileViewModel(
    private val userLoggedRepository: UserLoggedRepository,
    private val sharedPrefRepository: SharedPrefRepository
): BaseViewModel() {

    private val viewStateProfile: MutableLiveData<ViewStateModel<User>> = MutableLiveData()
    fun getProfileViewState() = viewStateProfile

    private val viewStateUpdate: MutableLiveData<ViewStateModel<Session>> = MutableLiveData()
    fun getProfileUpdateViewState() = viewStateUpdate

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun getProfile() {
        viewStateProfile.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(userLoggedRepository.getProfile()
            .subscribe({ baseModel ->
                viewStateProfile.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data ))
            }, {
                viewStateProfile.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }

    fun updateProfile(userRequest: UserRequest) {
        viewStateProfile.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(userLoggedRepository.updateProfile(userRequest)
            .subscribe({ baseModel ->
                sharedPrefRepository.saveSession(PREFS_KEY_USER_SESSION, baseModel.data)
                viewStateUpdate.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data ))
            }, {
                viewStateUpdate.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }
}