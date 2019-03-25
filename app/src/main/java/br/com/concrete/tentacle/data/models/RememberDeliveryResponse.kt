package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestedBy(
    val name: String
) : Parcelable

@Parcelize
data class RememberDeliveryResponse(
    val _id: String,
    val requestedBy: RequestedBy
) : Parcelable