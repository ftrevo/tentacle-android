package br.com.concrete.tentacle.data.models.filter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterItem(
    val title: String,
    val icon: String,
    val subItems: List<SubItem>
) : Parcelable