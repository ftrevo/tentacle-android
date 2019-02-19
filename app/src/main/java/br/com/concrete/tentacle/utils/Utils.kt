package br.com.concrete.tentacle.utils

import android.content.Context
import android.content.res.Resources

object Utils {

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun getJson(context: Context, path: String): String =
        context
            .resources
            .assets
            .open(path).bufferedReader().use { it.readText() }
}