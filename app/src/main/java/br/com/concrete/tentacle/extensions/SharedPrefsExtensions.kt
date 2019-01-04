package br.com.concrete.tentacle.extensions

import android.content.SharedPreferences

inline fun <reified T> SharedPreferences.put(key: String, value: T) {
    val editor = this.edit()

    when (T::class) {
        String::class -> editor.putString(key, value as String)
        Boolean::class -> editor.putBoolean(key, value as Boolean)
    }

    editor.apply()
}

inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    when (T::class) {
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