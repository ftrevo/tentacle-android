package br.com.concrete.tentacle.data.models.game

import com.google.gson.annotations.SerializedName

data class Cover(
    @SerializedName("imageId")
    val imageId: String?
)