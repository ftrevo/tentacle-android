package br.com.concrete.tentacle.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import com.google.gson.GsonBuilder
import retrofit2.HttpException
import java.io.IOException

abstract class BaseViewModel : ViewModel(){

    protected val success = MutableLiveData<Boolean>()

    protected val listErrors = MutableLiveData<List<String>>()

    fun getError() = listErrors as LiveData<List<String>>

    protected fun notKnownError(error: Throwable): ErrorResponse {

        val gson = GsonBuilder().create()
        var e = ErrorResponse()

        when(error){
            is HttpException -> {
                e = gson.fromJson(
                    error.response().errorBody()!!.charStream(),
                    ErrorResponse::class.java)
            }

            is IOException -> {
                e.message.add("No Internet Connection.")
                Log.e("ERROR TAG: ", "No Internet Connection.")
            }
        }

        return e
    }
}


