package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class State(
    val _id: String,
    var initials: String,
    var name: String
): Parcelable