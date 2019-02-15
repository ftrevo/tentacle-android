package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import br.com.concrete.tentacle.data.enums.Platform
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(
    val _id: String,
    val platform: Platform,
    val game: Game,
    val owner: Owner,
    val createdAt: String,
    val updatedAt: String
): Parcelable
