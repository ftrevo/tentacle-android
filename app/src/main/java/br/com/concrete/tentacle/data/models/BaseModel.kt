package br.com.concrete.tentacle.data.models

data class BaseModel<T>(
    val message: List<String>?,
    val data: T?
)