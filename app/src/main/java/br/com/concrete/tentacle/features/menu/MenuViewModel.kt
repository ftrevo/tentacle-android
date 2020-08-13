package br.com.concrete.tentacle.features.menu

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract
import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.extensions.fromJson
import br.com.concrete.tentacle.extensions.toJson
import br.com.concrete.tentacle.utils.SingleEvent
import br.com.concrete.tentacle.utils.LogWrapper
import br.com.concrete.tentacle.utils.PREFS_KEY_PATH_PHOTO
import br.com.concrete.tentacle.utils.PREFS_KEY_USER
import com.google.gson.Gson

class MenuViewModel(
    private val sharedPrefRepository: SharedPrefRepositoryContract,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val stateModel: MutableLiveData<SingleEvent<ViewStateModel<User>>> = MutableLiveData()
    fun getUser() = stateModel

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun loadUser() {
        val user = sharedPrefRepository.getStoredUser(PREFS_KEY_USER)
        user?.let {
            stateModel.postValue(SingleEvent(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = it)))
        } ?: run {
            loadUserFromServer()
        }
    }

    private fun loadUserFromServer() {
        disposables.add(
            userRepository.getProfile().subscribe({
                sharedPrefRepository.saveUser(PREFS_KEY_USER, it.data)
                stateModel.postValue(SingleEvent(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = it.data)))
            }, {
                LogWrapper.log("UserProfile: ", it.localizedMessage.toString())
            })
        )
    }

    fun savePathUserPhoto(idUser: String, path: String) {
        val hashMap: HashMap<String, String> = getHashMap()
        hashMap[idUser] = path
        sharedPrefRepository.saveString(key = PREFS_KEY_PATH_PHOTO, value = hashMap.toJson())
    }

    fun getPathPhotoUser(idUser: String): String? {
        val hashMap: HashMap<String, String> = getHashMap()
        return hashMap[idUser]
    }

    private fun getHashMap(): HashMap<String, String> {
        val hash = sharedPrefRepository.getStoreString(PREFS_KEY_PATH_PHOTO)!!

        return if (hash != "") {
            Gson().fromJson<HashMap<String, String>>(hash)
        } else {
            HashMap()
        }
    }

    fun removeSession() {
        sharedPrefRepository.removeSession()
    }

    fun removeUser() {
        sharedPrefRepository.removeUser()
    }

    fun updateUser(user: User) {
        sharedPrefRepository.updateUser(user)
    }
}