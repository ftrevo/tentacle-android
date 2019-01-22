package br.com.concrete.tentacle.data.repositories

import android.content.SharedPreferences
import br.com.concrete.tentacle.extensions.fromJson
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.extensions.get
import br.com.concrete.tentacle.extensions.put
import br.com.concrete.tentacle.extensions.remove
import br.com.concrete.tentacle.testing.OpenForTesting
import com.google.gson.Gson

@OpenForTesting
class SharedPrefRepository(private val mSharedPref: SharedPreferences) {

    fun saveString(key: String, value: String) = mSharedPref.put(key, value)

    fun saveSession(key: String, session: Session) = mSharedPref.put(key, Gson().toJson(session))

    fun getStoredSession(key: String): Session? {
        val sessionJson = mSharedPref.get(key, "")
        return if (sessionJson != "") {
            Gson().fromJson<Session>(sessionJson)
        } else {
            null
        }
    }

    fun saveUser(key: String, user: User)  = mSharedPref.put(key, Gson().toJson(user))

    fun getStoreString(key: String): String? = mSharedPref.get(key, "")

    fun deleteStoreString(key: String) = mSharedPref.remove(key)
}
