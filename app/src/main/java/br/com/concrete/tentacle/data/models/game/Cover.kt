package br.com.concrete.tentacle.data.models.game

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Cover(
    @SerializedName("image_id")
    val imageId: String?
): Serializable