package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val _id: String = String(),
    var name: String = String(),
    val email: String = String(),
    val phone: String = String(),
    var password: String = String(),
    var state: State = State(),
    var city: String = String(),
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