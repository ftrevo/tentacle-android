package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.BuildConfig
import br.com.concrete.tentacle.data.models.RequestRefreshToken
import br.com.concrete.tentacle.data.network.ApiService
import br.com.concrete.tentacle.data.network.ApiServiceAuthentication
import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import com.google.gson.Gson
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L
private const val TOKEN_AUTHORIZATION = "Authorization"
private const val APP_VERSION = "app-version"

const val API_WITHOUT_TOKEN = "apiWithoutToken"
const val API_WITH_TOKEN = "apiWithToken"

const val PROPERTY_BASE_URL = "PROPERTY_BASE_URL"

val networkModule = module {

    single {
        val auth = Authenticator { route, response ->
            val prefs: SharedPrefRepositoryContract = get()
            val userSession =
                prefs.getStoredSession(PREFS_KEY_USER_SESSION)

            userSession?.let {
                if (!response.request().header("Authorization").equals("${userSession.tokenType} ${userSession.accessToken}")) {
                    null
                }

                val apiServiceAuthentication: ApiServiceAuthentication = get(API_WITHOUT_TOKEN)
                val request = apiServiceAuthentication.refreshToken(RequestRefreshToken(it.refreshToken))
                val retrofitResponse = request.execute()

                if (retrofitResponse != null) {
                    val refreshTokenResponse = retrofitResponse.body()

                    refreshTokenResponse?.let {
                        val userSession = it.data
                        prefs.saveSession(it.data)
                        response.request().newBuilder()
                            .header(TOKEN_AUTHORIZATION, "${userSession.tokenType} ${userSession.accessToken}")
                            .build()
                    } ?: run {
                        prefs.removeSession()
                        prefs.removeUser()
                        null
                    }
                } else {
                    prefs.removeSession()
                    prefs.removeUser()
                    null
                }
            }
        }
        auth
    }

    single("withToken") {

        val httpLogInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            httpLogInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLogInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val tokenInterceptor = Interceptor { chain ->
            val prefs: SharedPrefRepositoryContract = get()

            val userSession =
                prefs.getStoredSession(PREFS_KEY_USER_SESSION)

            if (userSession != null) {
                val newRequest = chain.request()
                    .newBuilder()
                    .header(TOKEN_AUTHORIZATION, "${userSession.tokenType} ${userSession.accessToken}")
                    .header(APP_VERSION, BuildConfig.VERSION_CODE.toString())
                    .build()
                chain.proceed(newRequest)
            } else {
                chain.proceed(chain.request())
            }
        }

        OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .authenticator(get())
            .addInterceptor(httpLogInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()
    }

    single("retrofitWithToken") {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(getProperty<String>(PROPERTY_BASE_URL))
            .client(get("withToken"))
            .build()
    }

    single(API_WITH_TOKEN) {
        val retrofit: Retrofit = get("retrofitWithToken")
        retrofit.create<ApiService>(ApiService::class.java)
    }

    single("withoutToken") {

        val httpLogInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            httpLogInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLogInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val tokenInterceptor = Interceptor { chain ->
            val newRequest = chain.request()
                .newBuilder()
                .header(APP_VERSION, BuildConfig.VERSION_CODE.toString())
                .build()
            chain.proceed(newRequest)
        }

        OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(httpLogInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()
    }

    single("retrofitWithoutToken") {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(getProperty<String>(PROPERTY_BASE_URL))
            .client(get("withoutToken"))
            .build()
    }

    single(API_WITHOUT_TOKEN) {
        val retrofit: Retrofit = get("retrofitWithoutToken")
        retrofit.create<ApiServiceAuthentication>(ApiServiceAuthentication::class.java)
    }
}