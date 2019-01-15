package br.com.concrete.tentacle.di

import android.content.SharedPreferences
import br.com.concrete.tentacle.BuildConfig
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.network.ApiService
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 15L
private const val READ_TIMEOUT = 30L
private const val TOKEN_AUTHORIZATION = "Authorization"

const val PROPERTY_BASE_URL = "PROPERTY_BASE_URL"

val networkModule = module {

    single {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val tokenInterceptor = Interceptor { chain ->
            val prefs: SharedPrefRepository = get()
            val userSession = prefs.getStoredSession(PREFS_KEY_USER_SESSION)

            if (userSession != null) {
                val newRequest = chain.request()
                    .newBuilder()
                    .header(TOKEN_AUTHORIZATION, "${userSession.tokenType} ${userSession.accessToken}")
                    .build()
                chain.proceed(newRequest)
            } else {
                chain.proceed(chain.request())
            }
        }

        OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(getProperty<String>(PROPERTY_BASE_URL))
            .client(get())
            .build()
    }

    single {
        val retrofit: Retrofit = get()
        retrofit.create<ApiService>(ApiService::class.java)
    }
}