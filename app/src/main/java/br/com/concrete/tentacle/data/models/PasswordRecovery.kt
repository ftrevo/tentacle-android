package br.com.concrete.tentacle.data.models

data class PasswordRecovery(
    val email: String,
    val token: String,
    val password: String
)