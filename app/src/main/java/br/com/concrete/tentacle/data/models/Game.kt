package br.com.concrete.tentacle.data.models

import br.com.concrete.tentacle.data.models.game.Cover
import br.com.concrete.tentacle.data.models.game.GameMode
import br.com.concrete.tentacle.data.models.game.Genre
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Game(
    val _id: String = String(),
    @SerializedName("name", alternate = ["title"])
    val name: String = String(),
    val createdBy: User,
    var createdAt: String,
    var updateAt: String,
    @SerializedName("aggregated_rating")
    val rating: Float? = .0f,
    @SerializedName("first_release_date")
    val releaseDate: String? = null,
    @SerializedName("summary")
    val summary: String? = null,
    @SerializedName("game_modes")
    val gameModes: List<GameMode>? = null,
    @SerializedName("genres")
    val genres: List<Genre>? = null,
    @SerializedName("cover")
    val cover: Cover? = null

) : Serializable {

    companion object {

        const val ID_EMPTY_GAME = "-1"

        fun getEmptyGame(): Game = Game(
            ID_EMPTY_GAME,
            "",
            User(
                city = "",
                name = "",
                password = "",
                state = State(
                    "",
                    "",
                    ""
                )
            ),
            "",
            ""
        )
    }
}
