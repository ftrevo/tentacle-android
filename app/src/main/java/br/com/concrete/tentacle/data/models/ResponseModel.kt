package br.com.concrete.tentacle.data.models

data class ResponseModel<D,L>(
    val message: List<String>,
    val data: D,
    val list: L
)