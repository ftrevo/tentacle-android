package br.com.concrete.tentacle.data.models.library

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaLibrary(
    val _id: String,
    val owner: String,
    val ownerName: String
) : Parcelable {
    override fun toString(): String {
        return ownerName
    }
}