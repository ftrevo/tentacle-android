package br.com.concrete.tentacle.data.models

data class BaseModel<D>(
    val message: List<String>,
    val data: D
)