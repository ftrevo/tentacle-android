package br.com.concrete.tentacle.features.register

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.UserRepositoryContract

class RegisterUserViewModel(private val userRepositoryContract: UserRepositoryContract) :
    BaseViewModel() {

    private val viewStateState: MutableLiveData<ViewStateModel<ArrayList<State>>> = MutableLiveData()
    private val viewStateCity: MutableLiveData<ViewStateModel<ArrayList<String>>> = MutableLiveData()
    private val viewStateUser: MutableLiveData<ViewStateModel<User>> = MutableLiveData()

    init {
        loadStates()
    }

    fun getUser() = viewStateUser
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

        viewStateUser.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        userRepositoryContract.registerUser(userRequest, {base ->
            viewStateUser.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data))
        }, {
            viewStateUser.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors =  notKnownError(it)))
        })
    }

    fun loadCities(stateId: String){
        viewStateCity.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        userRepositoryContract.getCities(stateId, {base ->
            viewStateCity.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data.cities as ArrayList<String>))
        }, {
            viewStateCity.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors =  notKnownError(it)))
        })
    }

    private fun loadStates(){
        viewStateState.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        userRepositoryContract.getStates({base ->
            viewStateState.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = base.data.list as ArrayList<State>))
        },{
            viewStateState.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors =  notKnownError(it)))
        })
    }

    override fun onCleared() {
        userRepositoryContract.disposeAll()
        super.onCleared()
    }
}
