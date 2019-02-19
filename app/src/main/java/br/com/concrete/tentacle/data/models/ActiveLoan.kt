package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActiveLoan(
    val _id: String,
    val requestedAt: String,
    val loanDate: String?
) : Parcelable
