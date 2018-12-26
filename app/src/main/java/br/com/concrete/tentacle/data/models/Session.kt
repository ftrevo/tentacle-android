package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
): Parcelable