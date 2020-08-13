package br.com.concrete.tentacle.data.models.library

data class LibraryResponse(
    val list: ArrayList<Library>,
    val count: Int = 0
)