package br.com.concrete.tentacle.features.register

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.AppTentacle
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.utils.Event
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION

class RegisterUserViewModel(
    private val userRepository: UserRepository,
    private val sharedPrefRepository: SharedPrefRepository
) :
    BaseViewModel() {

    private val viewStateState: MutableLiveData<ViewStateModel<ArrayList<State>>> = MutableLiveData()
    private val viewStateCity: MutableLiveData<ViewStateModel<ArrayList<String>>> = MutableLiveData()
    private val viewStateUser: MutableLiveData<Event<ViewStateModel<Session>>> = MutableLiveData()

    fun getUser(): LiveData<Event<ViewStateModel<Session>>> = viewStateUser
    fun getStates() = viewStateState
    fun getCities() = viewStateCity

    fun registerUser(user: User) {
        val userRequest = UserRequest(
            name = user.name,
            city = user.city,
            email = user.email,
            state = user.state._id,
            phone = user.phone,
            password = user.password,
            deviceToken = AppTentacle.TOKEN
        )

        viewStateUser.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(userRepository.registerUser(userRequest).subscribe({ base ->
            sharedPrefRepository.saveSession(PREFS_KEY_USER_SESSION, base.data)
            viewStateUser.postValue(Event(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data)))
        }, {
            viewStateUser.postValue(Event(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it))))
        }))
    }

    fun loadCities(stateId: String) {
        viewStateCity.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(userRepository.getCities(stateId).subscribe({ base ->
            viewStateCity.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data.cities as ArrayList<String>))
        }, {
            viewStateCity.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
        }))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadStates() {
        viewStateState.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(userRepository.getStates().subscribe({ base ->
            viewStateState.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data.list as ArrayList<State>))
        }, {
            viewStateState.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
        }))
    }
}
