package br.com.concrete.tentacle.features.user

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.data.repositories.UserRepositoryContract

class UserViewModel (private val userRepositoryContract: UserRepositoryContract): ViewModel(){

    private val user = MutableLiveData<User>()

    fun retriveUser() = user as LiveData<User>

    @SuppressLint("CheckResult")
    fun registerUser(user: User){
        userRepositoryContract.registerUser(user)
    }



}
