package br.com.concrete.tentacle.features.menu

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.utils.Event
import br.com.concrete.tentacle.utils.PREFS_KEY_USER

class MenuViewModel(
    private val sharedPrefRepository: SharedPrefRepository
): BaseViewModel(){

    private val stateModel: MutableLiveData<ViewStateModel<User>> = MutableLiveData()
    fun getUser() = stateModel

    fun loadUser(){
        val user = sharedPrefRepository.getStoredUser(PREFS_KEY_USER)
        user?.let{
            stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = it))
        } ?: run {
            stateModel.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, errors = ErrorResponse()))
        }
    }

    fun loadUserFromServer(){

    }
}