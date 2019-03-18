package br.com.concrete.tentacle.data.models.library

import java.io.Serializable

data class Video(
    val id: Int,
    val name: String,
    val video_id: String
) : Serializable