package br.com.concrete.tentacle.data.models.library.loan

import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.toDate
import java.io.Serializable
import java.util.Date


data class LoanResponse(
    val _id: String,
    val game: Game,
    val media: Media,
    val mediaOwner: MediaOwner,
    val requestedAt: String,
    val requestedBy: RequestedBy,
    val estimatedReturnDate: String?,
    val loanDate: String?

) : Serializable {

    enum class LoanState {
        PENDING, EXPIRED, ACTIVE
    }

    fun getLoanState() : LoanState{
        val estimated = estimatedReturnDate?.toDate()?.timeInMillis
        val now = Date().time
        loanDate?.let {
            estimated?.let {
                return if(now > it) LoanState.EXPIRED else LoanState.ACTIVE
            }
        } ?: run {
            return LoanState.PENDING
        }
    }
}
