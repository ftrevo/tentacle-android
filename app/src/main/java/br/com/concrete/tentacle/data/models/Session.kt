package br.com.concrete.tentacle.data.models

import com.google.gson.annotations.SerializedName

data class Session(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)