package br.com.concrete.tentacle.data.repositories

import android.content.Context
import android.content.SharedPreferences

class SharedPrefRepository(context: Context) {

    private val mSharedPref: SharedPreferences = context.getSharedPreferences("task", Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) = mSharedPref.edit().putString(key, value).apply()

    fun getStoreString(key: String): String? = mSharedPref.getString(key, "")

    fun deleteStoreString(key: String) = mSharedPref.edit().remove(key).apply()

}