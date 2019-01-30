package br.com.concrete.tentacle.data.models

data class RegisteredMediaResponse(
    val _id: String,
    val platform: String,
    val game: Game,
    val owner: User,
    val createdAt: String,
    val updateAt: String
)