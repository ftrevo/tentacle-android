package br.com.concrete.tentacle.data.models.game

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Screenshot(
    val id: String,
    val height: Int,
    val width: Int,
    @SerializedName("image_id")
    val imageId: String
) : Parcelable