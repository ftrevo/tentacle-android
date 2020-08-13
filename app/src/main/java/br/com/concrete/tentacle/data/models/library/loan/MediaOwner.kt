package br.com.concrete.tentacle.data.models.library.loan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaOwner(
    val _id: String,
    val name: String
) : Parcelable