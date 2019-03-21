package br.com.concrete.tentacle.features.profile

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.data.repositories.UserLoggedRepository
import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.utils.LogWrapper
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION

class ProfileViewModel(
    private val userLoggedRepository: UserLoggedRepository,
    private val userRepository: UserRepository,
    private val sharedPrefRepository: SharedPrefRepository
): BaseViewModel() {

    private val viewStateProfile: MutableLiveData<ViewStateModel<User>> = MutableLiveData()
    fun getProfileViewState() = viewStateProfile

    private val viewStateUpdate: MutableLiveData<ViewStateModel<Session>> = MutableLiveData()
    fun getProfileUpdateViewState() = viewStateUpdate

    private val viewStateState: MutableLiveData<ViewStateModel<ArrayList<State>>> = MutableLiveData()
    fun getStateViewState() = viewStateState

    private val viewStateCity: MutableLiveData<ViewStateModel<ArrayList<String>>> = MutableLiveData()
    fun getCityViewState() = viewStateCity

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun getProfile() {
        viewStateProfile.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(userLoggedRepository.getProfile()
            .subscribe({ baseModel ->
                viewStateProfile.postValue(ViewStateModel(
                    status = ViewStateModel.Status.SUCCESS,
                    model = baseModel.data ))
            }, {
                LogWrapper.print(it)
                viewStateProfile.postValue(ViewStateModel(
                    status = ViewStateModel.Status.ERROR,
                    errors = notKnownError(it)))
            })
        )
    }

    fun loadStates() {
        viewStateState.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(userRepository.getStates().subscribe({ base ->
            viewStateState.postValue(ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = base.data.list as ArrayList<State>))
        }, {
            LogWrapper.print(it)
            viewStateState.postValue(ViewStateModel(
                status = ViewStateModel.Status.ERROR,
                errors = notKnownError(it)))
        }))
    }

    fun loadCities(stateId: String) {
        viewStateCity.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(userRepository.getCities(stateId).subscribe({ base ->
            viewStateCity.postValue(ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = base.data.cities as ArrayList<String>))
        }, {
            LogWrapper.print(it)
            viewStateCity.postValue(ViewStateModel(
                status = ViewStateModel.Status.ERROR,
                errors = notKnownError(it)))
        }))
    }

    fun updateProfile(userId: String, userRequest: UserRequest) {
        viewStateProfile.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(userLoggedRepository.updateProfile(userId, userRequest)
            .subscribe({ baseModel ->
                sharedPrefRepository.saveSession(PREFS_KEY_USER_SESSION, baseModel.data)
                viewStateUpdate.postValue(ViewStateModel(
                    status = ViewStateModel.Status.SUCCESS,
                    model = baseModel.data ))
            }, {
                LogWrapper.print(it)
                viewStateUpdate.postValue(ViewStateModel(
                    status = ViewStateModel.Status.ERROR,
                    errors = notKnownError(it)))
            })
        )
    }
}