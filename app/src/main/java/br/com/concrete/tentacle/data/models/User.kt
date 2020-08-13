package br.com.concrete.tentacle.data.models

import java.io.Serializable

data class User(
    val _id: String = String(),
    var name: String,
    val email: String = String(),
    val phone: String = String(),
    var password: String,
    var state: State,
    var city: String,
    var createdAt: String = String(),
    var updatedAt: String = String(),
    var internalImage: String = String()
) : Serializable