package br.com.concrete.tentacle.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Calendar.format(outFormat: String): String {
    val out = SimpleDateFormat(outFormat, Locale.getDefault())
    return out.format(this.time)
}