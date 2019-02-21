package br.com.concrete.tentacle.data.models.library.loan

import java.io.Serializable

data class RequestedBy(
    val _id: String,
    val name: String
) : Serializable