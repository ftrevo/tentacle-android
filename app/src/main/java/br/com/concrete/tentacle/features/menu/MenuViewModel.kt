package br.com.concrete.tentacle.features.menu

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract
import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.utils.LogWrapper
import br.com.concrete.tentacle.utils.PREFS_KEY_USER

class MenuViewModel(
    private val sharedPrefRepository: SharedPrefRepositoryContract,
    private val userRepository: UserRepository
): BaseViewModel(){

    private val stateModel: MutableLiveData<ViewStateModel<User>> = MutableLiveData()
    fun getUser() = stateModel

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun loadUser(){
        val user = sharedPrefRepository.getStoredUser(PREFS_KEY_USER)
        user?.let{
            stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = it))
        } ?: run {
            loadUserFromServer()
        }
    }

    private fun loadUserFromServer(){
        disposables.add(
            userRepository.getProfile().subscribe({
                sharedPrefRepository.saveUser(PREFS_KEY_USER,it.data)
                stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = it.data))
            },{
                LogWrapper.log("UserProfile: ", it.localizedMessage.toString())
            })
        )
    }

    fun removeSession(){
        sharedPrefRepository.removeSession()
    }

    fun removeUser(){
        sharedPrefRepository.removeUser()
    }

    fun updateUser(user: User) {
        sharedPrefRepository.updateUser(user)
    }
}