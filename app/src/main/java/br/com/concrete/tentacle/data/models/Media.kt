package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import br.com.concrete.tentacle.data.enums.Platform
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(
    val _id: String,
    val platform: Platform,
    val activeLoan: ActiveLoan?,
    @SerializedName("game") val gameData: GameData?
) : Parcelable