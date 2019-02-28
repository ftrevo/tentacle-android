package br.com.concrete.tentacle.features.myreservations

import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.extensions.toDate
import java.util.*
import java.util.concurrent.TimeUnit

open class MyReservationBaseViewModel : BaseViewModel() {

    enum class LoanState {
        PENDING, EXPIRED, ACTIVE
    }

    fun getLoanState(estimatedReturnDate: String?, loanDate: String?) : LoanState{
        var loanState = LoanState.ACTIVE
        val estimated = estimatedReturnDate?.toDate()?.timeInMillis
        val now = Date().time
        estimated?.let {
            loanState = when {
                it < now -> LoanState.EXPIRED
                loanDate.isNullOrBlank() -> LoanState.PENDING
                else -> LoanState.ACTIVE
            }
        }
        return  loanState
    }

    fun getLoanDaysToReturn(estimatedReturnDate: String) : Int{
        val estimated = estimatedReturnDate.toDate().timeInMillis
        val now = Date().time
        val diff = estimated - now
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
    }
}
