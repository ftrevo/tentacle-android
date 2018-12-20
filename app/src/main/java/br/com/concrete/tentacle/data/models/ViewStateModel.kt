package br.com.concrete.tentacle.data.models

data class ViewStateModel<T> (
    val status: Status,
    val model: T? = null
) {
    enum class Status {
        LOADING, SUCCESS, ERROR
    }
}