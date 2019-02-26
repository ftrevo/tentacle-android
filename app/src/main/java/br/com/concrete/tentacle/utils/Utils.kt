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

    fun assembleGameImageUrl(sizeType: String, imageId: String, isMaximumType: Boolean) =
        "https://images.igdb.com/igdb/image/upload/t_$sizeType" +
                "${if (isMaximumType) IMAGE_SYZE_TYPE_MAXIMUM else EMPTY_STRING}/$imageId.jpg"
}