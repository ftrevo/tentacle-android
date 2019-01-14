package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(
    val _id: String = String(),
    val platform: String = String(),
    val game: Game,
    val owner: User,
    var createdAt: String,
    var updatedAt: String
) : Parcelable