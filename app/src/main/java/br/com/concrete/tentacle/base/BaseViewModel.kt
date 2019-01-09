package br.com.concrete.tentacle.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.utils.LogWrapper
import com.google.gson.GsonBuilder
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    protected fun notKnownError(error: Throwable): ErrorResponse {

        val gson = GsonBuilder().create()
        var errorResponse = ErrorResponse()

        when (error) {
            is HttpException -> {
                when (error.code()) {
                    HttpURLConnection.HTTP_BAD_REQUEST -> errorResponse = gson.fromJson(
                        error.response().errorBody()!!.charStream(),
                        ErrorResponse::class.java)
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        // TODO RELOAD SESSION - 401 IS UNAUTHORIZED BECAUSE THE SESSION HAS EXPIRED
                    }
                    else -> errorResponse.messageInt.add(R.string.unknow_error)
                }
            }
            is IOException -> {
                errorResponse.messageInt.add(R.string.no_internet_connection)
                LogWrapper.log("ERROR TAG: ", "No Internet Connection.")
            }
        }

        return errorResponse
    }
}