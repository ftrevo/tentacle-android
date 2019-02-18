package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
    val _id: String = String(),

    @SerializedName("title", alternate = ["name"])
    val name: String = String(),
    val createdBy: User,
    var createdAt: String,
    var updateAt: String
) : Parcelable
