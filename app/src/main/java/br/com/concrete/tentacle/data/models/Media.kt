package br.com.concrete.tentacle.data.models

import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_3DS
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_3DS_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_SWITCH
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_SWITCH_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_360
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_360_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_ONE
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_ONE_ABBREV

data class Media(
    val _id: String,
    val platform: String,
    val game: Game,
    val owner: Owner,
    val createdAt: String,
    val updatedAt: String
) {
    fun showPlatformName() =
        when (platform) {
            PLATFORM_XBOX_ONE -> PLATFORM_XBOX_ONE_ABBREV
            PLATFORM_XBOX_360 -> PLATFORM_XBOX_360_ABBREV
            PLATFORM_NINTENDO_SWITCH -> PLATFORM_NINTENDO_SWITCH_ABBREV
            PLATFORM_NINTENDO_3DS -> PLATFORM_NINTENDO_3DS_ABBREV
            else -> platform
        }
}