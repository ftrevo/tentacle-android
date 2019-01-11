package br.com.concrete.tentacle.data.models

data class Game(
    private val id: String,
    private val title: String,
    private val createdBy: Created,
    private val createdAt: String,
    private val updatedAt: String
)