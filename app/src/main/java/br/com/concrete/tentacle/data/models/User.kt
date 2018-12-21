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

    @SerializedName("state")
    var stateId: String = String(),

    var state_: State = State(),
    var city: String = String(),
    var createdAt: String = String(),
    var updatedAt: String = String()
) : Parcelable {
    fun setState(state: State){
        this.state_ = state
        this.stateId = state._id
    }

    override fun toString(): String {
        return "Hello $name!!! _id = $_id \n state: ${state_.initials} - stateId: $stateId"
    }
}