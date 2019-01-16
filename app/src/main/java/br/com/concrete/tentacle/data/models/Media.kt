package br.com.concrete.tentacle.data.models

data class Media(
    val _id: String,
    val platform: String,
    val game: Game,
    val owner: Owner,
    val createdAt: String,
    val updatedAt: String
)