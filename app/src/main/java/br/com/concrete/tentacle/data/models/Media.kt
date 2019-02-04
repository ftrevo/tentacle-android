package br.com.concrete.tentacle.data.models

import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_3DS_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_SWITCH_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_PS3_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_PS4_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_360_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_ONE_ABBREV

data class Media(
    val _id: String,
    val platform: Platform,
    val game: Game,
    val owner: Owner,
    val createdAt: String,
    val updatedAt: String
) {
    enum class Platform(val platformName: String) {
        PS3(PLATFORM_PS3_ABBREV),
        PS4(PLATFORM_PS4_ABBREV),
        XBOXONE(PLATFORM_XBOX_ONE_ABBREV),
        XBOX360(PLATFORM_XBOX_360_ABBREV),
        NINTENDOSWITCH(PLATFORM_NINTENDO_SWITCH_ABBREV),
        NINTENDO3DS(PLATFORM_NINTENDO_3DS_ABBREV);
    }
}
