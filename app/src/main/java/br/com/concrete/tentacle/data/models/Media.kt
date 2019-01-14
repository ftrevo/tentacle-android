package br.com.concrete.tentacle.data.models

data class Media(
    private val _id: String,
    private val platform: String,
    private val game: Game,
    private val owner: Owner,
    private val createdAt: String,
    private val updatedAt: String
)