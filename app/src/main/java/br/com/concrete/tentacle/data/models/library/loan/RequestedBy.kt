package br.com.concrete.tentacle.data.models.library.loan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestedBy(
    val _id: String,
    val name: String
) : Parcelable