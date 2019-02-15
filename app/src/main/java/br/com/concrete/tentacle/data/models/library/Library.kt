package br.com.concrete.tentacle.data.models.library

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Library(
    val _id: String,
    val createdAt: String,
    val createdBy: String,
    val mediaNintendo3ds: List<MediaLibrary>,
    val mediaNintendoSwitch: List<MediaLibrary>,
    val mediaPs3: List<MediaLibrary>,
    val mediaPs4: List<MediaLibrary>,
    val mediaXbox360: List<MediaLibrary>,
    val mediaXboxOne: List<MediaLibrary>,
    val title: String,
    val updatedAt: String
) : Parcelable