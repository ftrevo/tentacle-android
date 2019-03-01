package br.com.concrete.tentacle.data.models.game

import com.google.gson.annotations.SerializedName

data class Screenshot(
    val id: String,
    val height: Int,
    val width: Int,
    @SerializedName("image_id")
    val imageId: String
)