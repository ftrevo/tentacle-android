package br.com.concrete.tentacle.data.models.game

import com.google.gson.annotations.SerializedName

data class Cover(
    @SerializedName("image_id")
    val imageId: String?
)