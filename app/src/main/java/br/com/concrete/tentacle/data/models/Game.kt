package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
    val _id: String = String(),
    val title: String = String(),
    val createdBy: User,
    var createdAt: String,
    var updateAt: String
) : Parcelable {

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
