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

    fun assembleGameImageUrl(sizeType: String, imageId: String, isMaximumType: Boolean = false) =
        "https://images.igdb.com/igdb/image/upload/t_$sizeType" +
                "${if (isMaximumType) IMAGE_SIZE_TYPE_MAXIMUM else EMPTY_STRING}/$imageId.jpg"

    fun assembleGameImageUrlYouTube(imageId: String, qualityImage: String = "mqdefault") =
            "https://img.youtube.com/vi/$imageId/$qualityImage.jpg"

    fun assembleUrlYouTube(videoId: String) =
        "http://www.youtube.com/watch?v=$videoId"
}