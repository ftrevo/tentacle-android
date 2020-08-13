package br.com.concrete.tentacle.utils

import android.util.Log
import br.com.concrete.tentacle.BuildConfig

object LogWrapper {
    fun log(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun print(throwable: Throwable) {
        if (BuildConfig.DEBUG) throwable.printStackTrace()
    }
}