package br.com.concrete.tentacle.data.models.game

import com.google.gson.annotations.SerializedName

data class GameMode(
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("name")
    val name: String?
) {
    enum class Modes(val slug: String?) {
        SINGLE_PLAYER("single-player"), MULTIPLAYER("multiplayer")
    }
}