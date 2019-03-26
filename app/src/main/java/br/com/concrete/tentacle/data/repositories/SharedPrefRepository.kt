package br.com.concrete.tentacle.data.repositories

import android.content.SharedPreferences
import br.com.concrete.tentacle.extensions.fromJson
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.extensions.get
import br.com.concrete.tentacle.extensions.put
import br.com.concrete.tentacle.extensions.remove
import br.com.concrete.tentacle.utils.PREFS_KEY_USER
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import com.google.gson.Gson

class SharedPrefRepository(private val mSharedPref: SharedPreferences) {

    fun saveString(key: String, value: String) = mSharedPref.put(key, value)

    fun saveSession(key: String, session: Session) = mSharedPref.put(key, Gson().toJson(session))

    fun removeSession() = mSharedPref.remove(PREFS_KEY_USER_SESSION)
    fun removeUser() = mSharedPref.remove(PREFS_KEY_USER)

    fun getStoredSession(key: String): Session? {
        val sessionJson = mSharedPref.get(key, "")
        return if (sessionJson != "") {
            Gson().fromJson<Session>(sessionJson)
        } else {
            null
        }
    }

    fun getStoredUser(key: String): User? {
        val user = mSharedPref.get(key, "")
        return if (user != "") {
            Gson().fromJson<User>(user)
        } else {
            null
        }
    }

    fun updateUser(user: User) {
        removeUser()
        saveUser(PREFS_KEY_USER, user)
    }

    fun saveUser(key: String, user: User) = mSharedPref.put(key, Gson().toJson(user))

    fun getStoreString(key: String): String? = mSharedPref.get(key, "")

    fun deleteStoreString(key: String) = mSharedPref.remove(key)
}
