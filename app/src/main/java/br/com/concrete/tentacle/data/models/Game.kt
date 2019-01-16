package br.com.concrete.tentacle.data.models

data class Game(
    val _id: String,
    val title: String,
    val createdBy: Created,
    val createdAt: String,
    val updatedAt: String
)