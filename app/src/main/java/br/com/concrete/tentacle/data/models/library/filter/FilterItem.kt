package br.com.concrete.tentacle.data.models.library.filter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterItem(
    val title: String,
    val icon: String,
    val subitems: List<SubItem>
) : Parcelable