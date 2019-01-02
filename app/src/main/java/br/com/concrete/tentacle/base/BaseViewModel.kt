package br.com.concrete.tentacle.base

import androidx.lifecycle.ViewModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.utils.LogWrapper
import com.google.gson.GsonBuilder
import retrofit2.HttpException
import java.io.IOException

abstract class BaseViewModel : ViewModel(){

    protected fun notKnownError(error: Throwable): ErrorResponse {

        val gson = GsonBuilder().create()
        var errorResponse = ErrorResponse()

        when(error){
            is HttpException -> {
                errorResponse = gson.fromJson(
                    error.response().errorBody()!!.charStream(),
                    ErrorResponse::class.java)
            }

            is IOException -> {
                errorResponse.message.add("No Internet Connection.")
                LogWrapper.log("ERROR TAG: ", "No Internet Connection.")
            }
        }

        return errorResponse
    }
}


