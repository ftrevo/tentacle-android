package br.com.concrete.tentacle.data.models

import android.text.TextUtils
import br.com.concrete.tentacle.utils.DEFAULT_EXCEPTION_STATUS_CODE

data class ErrorResponse(
    var message: ArrayList<String> = ArrayList(),
    var messageInt: ArrayList<Int> = ArrayList(),
    var statusCode: Int = DEFAULT_EXCEPTION_STATUS_CODE
) {
    override fun toString() = TextUtils.join(", ", message)
}