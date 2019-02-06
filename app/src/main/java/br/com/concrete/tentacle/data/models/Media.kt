package br.com.concrete.tentacle.data.models

import br.com.concrete.tentacle.data.enums.Platform

data class Media(
    val _id: String,
    val platform: Platform,
    val game: Game,
    val owner: Owner,
    val createdAt: String,
    val updatedAt: String
)
