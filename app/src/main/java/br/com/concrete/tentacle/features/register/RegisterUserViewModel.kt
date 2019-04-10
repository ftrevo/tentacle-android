package br.com.concrete.tentacle.features.register

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.AppTentacle
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract
import br.com.concrete.tentacle.data.repositories.TokenRepository
import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.utils.SingleEvent
import br.com.concrete.tentacle.utils.LogWrapper

class RegisterUserViewModel(
    private val userRepository: UserRepository,
    private val sharedPrefRepository: SharedPrefRepositoryContract,
    private val tokenRepository: TokenRepository
) :
    BaseViewModel() {

    private val viewStateState: MutableLiveData<ViewStateModel<ArrayList<State>>> = MutableLiveData()
    private val viewStateCity: MutableLiveData<ViewStateModel<ArrayList<String>>> = MutableLiveData()
    private val viewStateUser: MutableLiveData<SingleEvent<ViewStateModel<Session>>> = MutableLiveData()

    fun getUser(): LiveData<SingleEvent<ViewStateModel<Session>>> = viewStateUser
    fun getStates() = viewStateState
    fun getCities() = viewStateCity

    fun registerUser(user: User) {
        val userRequest = UserRequest(
            name = user.name,
            city = user.city,
            email = user.email,
            state = user.state._id,
            phone = user.phone,
            password = user.password
        )

        viewStateUser.postValue(SingleEvent(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(userRepository.registerUser(userRequest).subscribe({ base ->
            sharedPrefRepository.saveSession(base.data)
            viewStateUser.postValue(SingleEvent(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data)))
            disposables.add(tokenRepository.sendToken(AppTentacle.TOKEN).subscribe({
                LogWrapper.log("TokenResponse: ", it.message[0])
            }, {
                LogWrapper.log("TokenResponse: ", it.localizedMessage.toString())
            }))
        }, {
            viewStateUser.postValue(SingleEvent(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it))))
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
