package br.com.concrete.tentacle.data.models.game

import android.os.Parcelable
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.utils.GAME_MODE_CO_OPERATIVE
import br.com.concrete.tentacle.utils.GAME_MODE_MMO
import br.com.concrete.tentacle.utils.GAME_MODE_MULTI_PLAYER
import br.com.concrete.tentacle.utils.GAME_MODE_SINGLE_PLAYER
import br.com.concrete.tentacle.utils.GAME_MODE_SPLIT_SCREEN
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class GameMode(
    val slug: String?,
    val name: String?
) : Parcelable {

    fun getIconResource(): Int? {
        var icon: Int? = null
        slug?.let {
            icon = when (it) {
                    GAME_MODE_SINGLE_PLAYER -> R.drawable.ic_game_mode_single_player
                    GAME_MODE_MULTI_PLAYER -> R.drawable.ic_game_mode_multiplayer
                    GAME_MODE_CO_OPERATIVE -> R.drawable.ic_game_mode_coperative
                    GAME_MODE_MMO -> R.drawable.ic_game_mode_mmo
                    GAME_MODE_SPLIT_SCREEN -> R.drawable.ic_game_mode_split_screen
                    else -> null
            }
        }
        return icon
    }
}