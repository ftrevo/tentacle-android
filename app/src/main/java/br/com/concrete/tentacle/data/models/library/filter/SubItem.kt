package br.com.concrete.tentacle.data.models.library.filter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubItem(
    var key: String,
    val keyValue: String,
    val name: String,
    var isChecked: Boolean = false
) : Parcelable