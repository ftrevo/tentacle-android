package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
    val _id: String = String(),
    val title: String = String(),
    val createdBy: User,
    var createdAt: String,
    var updateAt: String
) : Parcelable
