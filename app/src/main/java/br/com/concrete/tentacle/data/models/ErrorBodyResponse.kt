package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorBodyResponse(
    val message: List<String> = ArrayList<String>()
): Parcelable