package br.com.concrete.tentacle.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.utils.DEFAULT_EXCEPTION_STATUS_CODE
import br.com.concrete.tentacle.utils.LogWrapper
import com.google.gson.GsonBuilder
import io.reactivex.disposables.CompositeDisposable
import org.koin.standalone.KoinComponent
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

open class BaseViewModel : ViewModel(), LifecycleObserver, KoinComponent {

    protected val disposables = CompositeDisposable()
    private val sessionStatus: MutableLiveData<Int> = MutableLiveData()
    fun getSessionStatus() = sessionStatus

    protected fun notKnownError(error: Throwable): ErrorResponse {

        val gson = GsonBuilder().create()
        var errorResponse = ErrorResponse()

        when (error) {
            is HttpException -> {
                when (error.code()) {
                    HttpURLConnection.HTTP_BAD_REQUEST -> {
                        errorResponse = gson.fromJson(
                            error.response().errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )
                    }
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        sessionStatus.postValue(HttpURLConnection.HTTP_UNAUTHORIZED)
                    }
                    HttpURLConnection.HTTP_NOT_FOUND -> {
                        errorResponse = gson.fromJson(
                            error.response().errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )
                    }
                    else -> errorResponse.messageInt.add(R.string.unknow_error)
                }
                errorResponse.statusCode = error.code()
            }
            is IOException -> {
                errorResponse.statusCode = DEFAULT_EXCEPTION_STATUS_CODE
                errorResponse.messageInt.add(R.string.no_internet_connection)
                LogWrapper.log("ERROR TAG: ", "No Internet Connection.")
            }
        }

        return errorResponse
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}