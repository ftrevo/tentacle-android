package br.com.concrete.tentacle.data.models.game

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("name")
    val name: String?
)