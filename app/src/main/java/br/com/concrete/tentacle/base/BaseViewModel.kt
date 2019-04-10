package br.com.concrete.tentacle.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.eventPublisher.EventPublisherContract
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.utils.DEFAULT_EXCEPTION_STATUS_CODE
import br.com.concrete.tentacle.utils.HTTP_UPGRADE_REQUIRED
import br.com.concrete.tentacle.utils.LogWrapper
import com.google.gson.GsonBuilder
import io.reactivex.disposables.CompositeDisposable
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

open class BaseViewModel : ViewModel(), LifecycleObserver, KoinComponent {

    private val eventPublisher: EventPublisherContract by inject()

    protected val disposables = CompositeDisposable()

    protected fun notKnownError(error: Throwable): ErrorResponse? {

        val gson = GsonBuilder().create()
        var errorResponse:ErrorResponse? = null
        val msgsInt = ArrayList<Int>()

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
                        eventPublisher.publish(HttpURLConnection.HTTP_UNAUTHORIZED)
                    }
                    HttpURLConnection.HTTP_NOT_FOUND -> errorResponse = gson.fromJson(
                        error.response().errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    HTTP_UPGRADE_REQUIRED -> errorResponse = gson.fromJson(
                        error.response().errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    else -> {
                        msgsInt.add(R.string.unknow_error)
                        errorResponse = ErrorResponse(messageInt = msgsInt)
                    }
                }
                errorResponse?.statusCode = error.code()
            }
            is IOException -> {
                msgsInt.add(R.string.no_internet_connection)
                errorResponse = ErrorResponse(
                    statusCode = DEFAULT_EXCEPTION_STATUS_CODE,
                    messageInt = msgsInt)
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