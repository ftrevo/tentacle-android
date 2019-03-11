package br.com.concrete.tentacle.data.models.library

import android.os.Parcelable
import br.com.concrete.tentacle.data.models.game.Cover
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Library(
    val _id: String,
    val mediaNintendo3ds: List<MediaLibrary>,
    val mediaNintendoSwitch: List<MediaLibrary>,
    val mediaPs3: List<MediaLibrary>,
    val mediaPs4: List<MediaLibrary>,
    val mediaXbox360: List<MediaLibrary>,
    val mediaXboxOne: List<MediaLibrary>,
    val mediaNintendo3dsCount: Int,
    val mediaNintendoSwitchCount: Int,
    val mediaPs3Count: Int,
    val mediaPs4Count: Int,
    val mediaXbox360Count: Int,
    val mediaXboxOneCount: Int,

    @SerializedName("title", alternate = ["name"])
    val name: String,
    val cover: Cover? = null
) : Parcelable