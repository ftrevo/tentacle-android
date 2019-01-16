package br.com.concrete.tentacle.data.models

data class Media(
    val _id: String = String(),
    val platform: String = String(),
    val game: Game,
    val owner: User,
    var createdAt: String,
    var updatedAt: String
)