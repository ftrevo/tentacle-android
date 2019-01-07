package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val _id: String = String(),
    var name: String,
    val email: String,
    val phone: String,
    var password: String,
    var state: State,
    var city: String,
    var createdAt: String = String(),
    var updatedAt: String = String()
) : Parcelable