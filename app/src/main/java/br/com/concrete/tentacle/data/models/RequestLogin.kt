package br.com.concrete.tentacle.data.models

data class RequestLogin(
    val email: String,
    val password: String,
    val deviceToken: String?
)