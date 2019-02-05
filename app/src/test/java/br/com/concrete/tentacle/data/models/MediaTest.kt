package br.com.concrete.tentacle.data.models

import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_3DS_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_SWITCH_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_PS4_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_360_ABBREV
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MediaTest {

    fun getJson(path: String): String? {
        return this::class
            .java
            .classLoader?.getResource(
            path
        )?.readText()
    }

    @Test
    fun `media platform enum class getName should return abbreviation`() {
        val collectionType = object : TypeToken<BaseModel<Media>>() {}.type

        val switchJson = getJson("mockjson/models/response_media_switch.json")
        val switchResponse: BaseModel<Media> = GsonBuilder()
            .create()
            .fromJson(switchJson, collectionType)

        var actual = switchResponse.data.platform.platformName
        var expected = PLATFORM_NINTENDO_SWITCH_ABBREV

        assertEquals(expected, actual)

        val n3dsJson = getJson("mockjson/models/response_media_3ds.json")
        val n3dsResponse: BaseModel<Media> = GsonBuilder()
            .create()
            .fromJson(n3dsJson, collectionType)

        actual = n3dsResponse.data.platform.platformName
        expected = PLATFORM_NINTENDO_3DS_ABBREV

        assertEquals(expected, actual)

        val ps4Json = getJson("mockjson/models/response_media_ps4.json")
        val ps4Response: BaseModel<Media> = GsonBuilder()
            .create()
            .fromJson(ps4Json, collectionType)

        actual = ps4Response.data.platform.platformName
        expected = PLATFORM_PS4_ABBREV

        assertEquals(expected, actual)

        val x360Json = getJson("mockjson/models/response_media_x360.json")
        val x360Response: BaseModel<Media> = GsonBuilder()
            .create()
            .fromJson(x360Json, collectionType)

        actual = x360Response.data.platform.platformName
        expected = PLATFORM_XBOX_360_ABBREV

        assertEquals(expected, actual)
    }
}