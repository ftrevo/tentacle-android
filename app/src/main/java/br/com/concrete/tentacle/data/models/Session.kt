package br.com.concrete.tentacle.data.models


data class Session(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)