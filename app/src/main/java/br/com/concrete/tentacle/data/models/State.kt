package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class State(
    val _id: String = String(),
    var initials: String = String(),
    var name: String = String()
) : Parcelable