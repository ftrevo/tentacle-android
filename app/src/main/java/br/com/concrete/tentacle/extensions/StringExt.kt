package br.com.concrete.tentacle.extensions

import android.util.Patterns
import br.com.concrete.tentacle.utils.BLANk_SPACE_STRING
import br.com.concrete.tentacle.utils.EMPTY_STRING
import br.com.concrete.tentacle.utils.SIMPLE_DATE_INPUT_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar

fun String.validateEmail() = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.validatePassword() = this.isNotEmpty() && this.length > 5

fun String.digits() = this.replace("(", "")
    .replace(")", "")
    .replace("-", "")

fun String.toPlatformName() =
    when (this.toLowerCase()) {
        "playstation 3" -> "PS3"
        "playstation 4" -> "PS4"
        else -> toUpperCase().replace(BLANk_SPACE_STRING, EMPTY_STRING)
    }

fun String.toDate(): Calendar {
    val sdf = SimpleDateFormat(SIMPLE_DATE_INPUT_FORMAT)
    val date = sdf.parse(this)
    val cal = Calendar.getInstance()
    cal.time = date
    return cal
}