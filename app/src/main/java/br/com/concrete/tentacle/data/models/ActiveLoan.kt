package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.utils.DEFAULT_RETURN_DATE_IN_WEEKS
import br.com.concrete.tentacle.utils.ONE_HOUR
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ActiveLoan(
    val _id: String,
    val requestedAt: String,
    val loanDate: String?,
    val requestedByName: String?,
    val requestedByState: String?,
    val requestedByCity: String?,
    val estimatedReturnDate: String? = null
) : Parcelable {

    companion object {
        fun getDefaultReturnDate() = Calendar.getInstance()
    }

    private fun getReturnDate(): Calendar? {
        loanDate?.let {
            val date = it.toDate()
            date.add(Calendar.WEEK_OF_MONTH, DEFAULT_RETURN_DATE_IN_WEEKS)
            return date
        } ?: run {
            return null
        }
    }

    fun getReturnDateWithoutEstimatedReturn(): Calendar? {
        requestedAt.let {
            val date = it.toDate()
            date.add(Calendar.WEEK_OF_MONTH, DEFAULT_RETURN_DATE_IN_WEEKS)
            return date
        }
    }

    fun isExpired(): Boolean {
        getReturnDate()?.let {
            val currentDate = Calendar.getInstance()
            val days = daysBetweenDates(currentDate.time, it.time)
            return days < 1
        } ?: run {
            return false
        }
    }

    private fun daysBetweenDates(currentDate: Date, loanDate: Date) =
        ((loanDate.time - currentDate.time + ONE_HOUR) / (ONE_HOUR * 24))
}
