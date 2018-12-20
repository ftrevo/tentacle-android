package br.com.concrete.tentacle.features.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.concrete.tentacle.data.models.*
import br.com.concrete.tentacle.data.repositories.UserRepositoryContract

class UserViewModel(private val userRepositoryContract: UserRepositoryContract) :
    ViewModel(), UserViewModelContract {

    private val user = MutableLiveData<User>()
    private val listStates = MutableLiveData<List<State>>()
    private val listCities = MutableLiveData<List<String>>()
    private val listErrors = MutableLiveData<List<String>>()
    private val success = MutableLiveData<Boolean>()

    private var errors = mutableListOf<String>()

    init {
        loadStates()
    }

    fun getUser() = user as LiveData<User>
    fun getStates() = listStates as LiveData<List<State>>
    fun getCities() = listCities as LiveData<List<String>>
    fun getError() = listErrors as LiveData<List<String>>

    fun registerUser(user: User) {
        userRepositoryContract.registerUser(user, this as UserViewModelContract)
    }

    fun loadCities(stateId: String){
        userRepositoryContract.getCities(stateId, this as UserViewModelContract)
    }

    private fun loadStates(){
        userRepositoryContract.getStates(this as UserViewModelContract)
    }

    override fun callBackUser(base: BaseModel<User>) {
        if(base.data != null){
            this.user.value = base.data
        }else{
            onError(base.message!!)
        }
    }

    override fun callBackStates(base: BaseModel<ResponseState>) {
        if(base.data != null){
            this.listStates.value = base.data.list
        }else{
            onError(base.message!!)
        }
    }

    override fun callBackCities(base: BaseModel<ResponseCity>) {
        if(base.data != null){
            this.listCities.value = base.data.cities
        }else{
            onError(base.message!!)
        }
    }

    private fun onError(base: List<String>) {
        errors.addAll(base)
        this.listErrors.value = base
    }

    override fun notKnownError(error: Throwable) {
        errors.add(error.cause.toString())
        listErrors.value =  errors
    }

}
