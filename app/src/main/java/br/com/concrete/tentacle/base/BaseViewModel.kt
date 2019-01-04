package br.com.concrete.tentacle.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.utils.LogWrapper
import com.google.gson.GsonBuilder
import retrofit2.HttpException
import java.io.IOException

abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    protected fun notKnownError(error: Throwable): ErrorResponse {

        val gson = GsonBuilder().create()
        var errorResponse = ErrorResponse()

        when (error) {
            is HttpException -> {
                when (error.code()) {
                    400 -> errorResponse = gson.fromJson(
                        error.response().errorBody()!!.charStream(),
                        ErrorResponse::class.java)
                    401 -> {
                        // TODO RELOAD SESSION - 401 IS UNAUTHORIZED BECAUSE THE SESSION HAS EXPIRED
                    }
                    else -> errorResponse.message.add("Not Know Error.")
                }
            }
            is IOException -> {
                errorResponse.message.add("No Internet Connection.")
                LogWrapper.log("ERROR TAG: ", "No Internet Connection.")
            }
        }

        return errorResponse
    }
}