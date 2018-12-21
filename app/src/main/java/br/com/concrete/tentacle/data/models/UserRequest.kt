package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserRequest(
    val name: String = String(),
    val email: String = String(),
    val phone: String = String(),
    val password: String = String(),
    val state: String = String(),
    val city: String = String()
) : Parcelable {

}