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
import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract
import br.com.concrete.tentacle.data.repositories.UserLoggedRepository
import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.utils.LogWrapper
import br.com.concrete.tentacle.utils.PREFS_KEY_USER
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION

class ProfileViewModel(
    private val userLoggedRepository: UserLoggedRepository,
    private val userRepository: UserRepository,
    private val sharedPrefRepository: SharedPrefRepositoryContract
) : BaseViewModel() {

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
        sharedPrefRepository.getStoredUser(PREFS_KEY_USER)?.let {
            viewStateProfile.postValue(ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = it))
        } ?: run {
            loadUserFromServer()
        }
    }

    private fun loadUserFromServer(){
        disposables.add(
            userRepository.getProfile().subscribe({
                sharedPrefRepository.saveUser(PREFS_KEY_USER,it.data)
                viewStateProfile.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = it.data))
            },{
                LogWrapper.log("UserProfile: ", it.localizedMessage.toString())
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

    fun updateProfile(currentUser: User, userRequest: UserRequest) {
        viewStateProfile.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(userLoggedRepository.updateProfile(currentUser._id, userRequest)
            .subscribe({ baseModel ->
                sharedPrefRepository.saveSession(PREFS_KEY_USER_SESSION, baseModel.data)
                viewStateUpdate.postValue(ViewStateModel(
                    status = ViewStateModel.Status.SUCCESS,
                    model = baseModel.data))
                updateCurrentUser(currentUser, userRequest)
            }, {
                LogWrapper.print(it)
                viewStateUpdate.postValue(ViewStateModel(
                    status = ViewStateModel.Status.ERROR,
                    errors = notKnownError(it)))
            })
        )
    }

    private fun updateCurrentUser(currentUser: User, userRequest: UserRequest){
        var newState : State? = null
        userRequest.stateObj?.let {
            newState = it
        } ?: run {
            newState = currentUser.state
        }

        var imagePath = ""
        currentUser.internalImage?.let {
            imagePath = it
        }
        val user = User(_id = currentUser._id, name = userRequest.name, email = userRequest.email,
            phone = userRequest.phone, password = "",
            city = userRequest.city, createdAt = currentUser.createdAt,
            state = newState!!,
            updatedAt = currentUser.updatedAt,
            internalImage = imagePath)
        sharedPrefRepository.saveUser(PREFS_KEY_USER, user)
    }
}