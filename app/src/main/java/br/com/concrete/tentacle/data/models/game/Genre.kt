package br.com.concrete.tentacle.data.models.game

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(
    val name: String?
) : Parcelable