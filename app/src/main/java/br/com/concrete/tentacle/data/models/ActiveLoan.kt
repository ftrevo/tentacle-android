package br.com.concrete.tentacle.data.models

import android.os.Parcelable
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.utils.DEFAULT_RETURN_DATE_IN_WEEKS
import kotlinx.android.parcel.Parcelize
import java.util.Calendar

@Parcelize
data class ActiveLoan(
    val _id: String,
    val requestedAt: String,
    val loanDate: String?,
    val requestedByName: String?,
    val requestedByState: String?,
    val requestedByCity: String?
) : Parcelable {

    companion object {
        fun getDefaultReturnDate(): Calendar{
            val date = Calendar.getInstance()
            date.add(Calendar.WEEK_OF_MONTH, DEFAULT_RETURN_DATE_IN_WEEKS)
            return date
        }
    }

    fun getReturnDate(): Calendar? {
        loanDate?.let {
            val date = it.toDate()
            date.add(Calendar.WEEK_OF_MONTH, DEFAULT_RETURN_DATE_IN_WEEKS)
            return date
        } ?: run {
            return null
        }
    }

    fun isExpired(): Boolean {
        getReturnDate()?.let {
            val currentDate = Calendar.getInstance()
            return currentDate.timeInMillis > it.timeInMillis
        } ?: run{
            return false
        }
    }

}
