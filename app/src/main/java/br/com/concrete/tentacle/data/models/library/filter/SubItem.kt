package br.com.concrete.tentacle.data.models.library.filter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubItem(
    var queryType: String,
    val queryParameter: String,
    val name: String,
    var isChecked: Boolean = false
) : Parcelable