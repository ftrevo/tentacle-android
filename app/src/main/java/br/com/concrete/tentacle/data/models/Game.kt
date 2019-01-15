package br.com.concrete.tentacle.data.models

data class Game(
    val _id: String = String(),
    val title: String = String(),
    val createdBy: User,
    var createdAt: String,
    var updateAt: String
)