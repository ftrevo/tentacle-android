package br.com.concrete.tentacle.utils.deserializer

import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.Owner
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class LibraryDeserializer : JsonDeserializer<LibraryResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LibraryResponse {

        val list = ArrayList<Library>()

        json?.let {
            val jsonArray = it.asJsonObject.get("list").asJsonArray
            jsonArray?.let { array ->
                array.forEach {
                    val library = createLibrary(it)
                    list.addAll(library)
                }
            }
        }

        return LibraryResponse(list)
    }

    private fun createLibrary(jsonElement: JsonElement): List<Library> {
        val list = ArrayList<Library>()

        val media = createMediaWithOwner(jsonElement)
        media.forEach { mediaItem ->
            val library = Library()
            library._id = jsonElement.asJsonObject.get("_id").asString
            library.title = jsonElement.asJsonObject.get("title").asString
            library.media = mediaItem

            list.add(library)
        }

        return list
    }

    private fun createMediaWithOwner(jsonElement: JsonElement): List<Media> {
        val list = ArrayList<Media>()

        val mediasList = setOf(
                                Pair("mediaPs3", "PS3"),
                                Pair("mediaPs4", "PS4"),
                                Pair("mediaXbox360", "Xbox360"),
                                Pair("mediaXboxOne", "XOne"),
                                Pair("mediaNintendo3ds", "3DS"),
                                Pair("mediaNintendoSwitch", "Switch")
                            )

        mediasList.forEach { mediaName ->
            val element = jsonElement.asJsonObject.get(mediaName.first).asJsonArray
            element.forEach { media ->
                list.add(
                    Media(
                        _id = media.asJsonObject.get("_id").asString,
                        platform = mediaName.second,
                        owner = Owner(
                                    media.asJsonObject.get("owner").asString,
                                    media.asJsonObject.get("ownerName").asString
                                )
                    )
                )
            }
        }
        return list
    }
}