package br.com.concrete.tentacle.data.models.library.loan

import android.os.Parcelable
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.extensions.toDate
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class LoanResponse(
    val _id: String,
    val game: Game,
    val media: Media,
    val mediaOwner: MediaOwner,
    val requestedAt: String,
    val requestedBy: RequestedBy,
    val estimatedReturnDate: String?,
    val loanDate: String?,
    val returnDate: String?

) : Parcelable {

    enum class LoanState {
        PENDING, EXPIRED, ACTIVE
    }

    fun getLoanState(): LoanState {
        val estimated = estimatedReturnDate?.toDate()?.timeInMillis
        val now = Date().time
        loanDate?.let {
            estimated?.let {
                return if (now > it) LoanState.EXPIRED else LoanState.ACTIVE
            }
        } ?: run {
            return LoanState.PENDING
        }
    }
}
