package br.com.concrete.tentacle.data.models

import br.com.concrete.tentacle.data.models.game.Cover
import br.com.concrete.tentacle.data.models.game.GameMode
import br.com.concrete.tentacle.data.models.game.Genre
import br.com.concrete.tentacle.data.models.game.Screenshot
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Game(
    val _id: String = String(),
    @SerializedName("name", alternate = ["title"])
    val name: String = String(),
    val createdBy: String,
    var createdAt: String,
    var updateAt: String,
    @SerializedName("aggregated_rating")
    val rating: Float? = .0f,
    @SerializedName("first_release_date")
    val releaseDate: String? = null,
    val summary: String? = null,
    @SerializedName("game_modes")
    val gameModes: List<GameMode>? = null,
    val genres: List<Genre>? = null,
    val cover: Cover? = null,
    val screenshots: List<Screenshot>? = null

) : Serializable {

    companion object {

        const val ID_EMPTY_GAME = "-1"

        fun getEmptyGame(): Game = Game(
            ID_EMPTY_GAME,
            "",
            "",
            "",
            ""
        )
    }
}
