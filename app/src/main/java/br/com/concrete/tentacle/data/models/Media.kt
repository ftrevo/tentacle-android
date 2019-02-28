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
    val game: GameData?
) : Parcelable {

    companion object {
        const val ID_EMPTY_MEDIA = "-1"

        fun getEmptyMedia(): Media = Media(
            ID_EMPTY_MEDIA,
            Platform.PS4,
            ActiveLoan(ID_EMPTY_MEDIA,
                "",
                "",
                "",
                "",
                ""
            ),
            GameData(ID_EMPTY_MEDIA, "")
        )
    }

}