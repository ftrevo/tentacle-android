package br.com.concrete.tentacle.data.repositories


import android.content.SharedPreferences
import br.com.concrete.tentacle.extensions.fromJson
import br.com.concrete.tentacle.data.models.Session
import com.google.gson.Gson

inline fun <reified T> SharedPreferences.put(key: String, value: T) {
    val editor = this.edit()

    when(T::class) {
        String::class -> editor.putString(key, value as String)
        Boolean::class -> editor.putBoolean(key, value as Boolean)
    }

    editor.apply()
}

inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    when(T::class) {
        String::class -> return this.getString(key, defaultValue as String) as T
        else -> return defaultValue
    }
}

fun SharedPreferences.remove(key: String) {
    val editor = this.edit()
    editor
        .remove(key)
        .apply()
}

class SharedPrefRepository(private val mSharedPref: SharedPreferences) {

    fun saveString(key: String, value: String) = mSharedPref.put(key, value)

    fun saveSession(key: String, session: Session) = mSharedPref.put(key, session)

    fun getStoredSession(key: String): Session {
        val sessionJson = mSharedPref.get(key, "")
        return Gson().fromJson<Session>(sessionJson)
    }

    fun getStoreString(key: String): String? = mSharedPref.get(key, "")

    fun deleteStoreString(key: String) = mSharedPref.remove(key)

}
