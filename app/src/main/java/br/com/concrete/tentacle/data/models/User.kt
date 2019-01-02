package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val _id: String = String(),
    var name: String,
    val email: String,
    val phone: String,
    var password: String,
    var state: State,
    var city: String,
    var createdAt: String = String(),
    var updatedAt: String = String()
) : Parcelable {
    override fun toString(): String {
        return "Name: $name!!! \n" +
                "State: ${state.initials} \n" +
                "City: $city \n" +
                "E-mail: $email \n" +
                "Password: $password \n" +
                "Phone: $phone \n"
    }
}