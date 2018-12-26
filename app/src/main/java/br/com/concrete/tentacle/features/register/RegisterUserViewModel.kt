package br.com.concrete.tentacle.features.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.concrete.tentacle.data.models.*
import br.com.concrete.tentacle.data.repositories.UserRepositoryContract
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class RegisterUserViewModel(private val userRepositoryContract: UserRepositoryContract) :
    ViewModel() {

    private val gson = Gson()

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

        var userRequest = UserRequest(
            name = user.name,
            city = user.city,
            email = user.email,
            state = user.state._id,
            phone = user.phone,
            password = user.password
        )

        userRepositoryContract.registerUser(userRequest, {base ->
            if(base.data != null){
                success.value = true
                this.user.value = base.data
            }else{
                onError(base.message!!)
            }
        }, {
            notKnownError(it)
        })
    }

    fun loadCities(stateId: String){
        userRepositoryContract.getCities(stateId, {base ->
            if(base.data != null){
                success.value = true
                this.listCities.value = base.data.cities
            }else{
                onError(base.message!!)
            }

        }, {
            notKnownError(it)
        })
    }

    private fun loadStates(){
        userRepositoryContract.getStates({base ->
            if(base.data != null){
                success.value = true
                this.listStates.value = base.data.list
            }else{
                onError(base.message!!)
            }
        },{
            notKnownError(it)
        })
    }

    private fun onError(base: List<String>) {
        success.value = false
        errors.addAll(base)
        this.listErrors.value = base
    }

    private fun notKnownError(error: Throwable) {
        success.value = false
        var e = ErrorBodyResponse()

        when(error){
            is HttpException -> {
                e = gson.fromJson(
                error.response().errorBody()!!.charStream(),
                ErrorBodyResponse::class.java)
            }

            is IOException -> {
                e.message.add("No Internet Connection.")
                Log.e("ERROR TAG: ", "No Internet Connection.")
            }
        }

        errors.addAll(e.message)
        this.listErrors.value = errors
    }

    override fun onCleared() {
        userRepositoryContract.disposeAll()
        super.onCleared()
    }

}
