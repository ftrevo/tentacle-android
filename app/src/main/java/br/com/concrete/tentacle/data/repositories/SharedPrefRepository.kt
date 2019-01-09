package br.com.concrete.tentacle.data.repositories

import android.content.SharedPreferences
import br.com.concrete.tentacle.extensions.fromJson
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.extensions.get
import br.com.concrete.tentacle.extensions.put
import br.com.concrete.tentacle.extensions.remove
import com.google.gson.Gson

class SharedPrefRepository(private val mSharedPref: SharedPreferences) {

    fun saveString(key: String, value: String) = mSharedPref.put(key, value)

    fun saveSession(key: String, session: Session) = mSharedPref.put(key, Gson().toJson(session))

    fun getStoredSession(key: String): Session {
        val sessionJson = mSharedPref.get(key, "")
        return Gson().fromJson<Session>(sessionJson)
    }

    fun getStoreString(key: String): String? = mSharedPref.get(key, "")

    fun deleteStoreString(key: String) = mSharedPref.remove(key)
}
