package br.com.concrete.tentacle.data.models

import android.text.TextUtils

data class ErrorResponse(
    var message: ArrayList<String> = ArrayList()
) {
    override fun toString() = TextUtils.join(", ", message)
}