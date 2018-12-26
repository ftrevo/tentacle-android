package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorBodyResponse(
    var message: ArrayList<String> = ArrayList<String>()
): Parcelable