import br.com.concrete.tentacle.BuildConfig
import br.com.concrete.tentacle.data.network.ApiServiceAuthentication
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

const val API_WITHOUT_TOKEN = "apiWithoutToken"
const val API_WITH_TOKEN = "apiWithToken"

const val PROPERTY_BASE_URL = "PROPERTY_BASE_URL"

val networkModule = module {

    single{
        val httpLogInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            httpLogInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLogInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        httpLogInterceptor
    }

    single("tokenInterceptor"){
        Interceptor { chain ->
            val prefs: SharedPrefRepository = get()
            val userSession
                    = prefs.getStoredSession(PREFS_KEY_USER_SESSION)
            userSession?.let {
                val newRequest = chain.request()
                    .newBuilder()
                    .header(TOKEN_AUTHORIZATION, "${userSession.tokenType} ${userSession.accessToken}")
                    .build()
                chain.proceed(newRequest)
            }
        }
    }

    single("withToken"){
        OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(get())
            .addInterceptor(get("tokenInterceptor"))
            .build()
    }

    single("retrofitWithToken"){
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(getProperty<String>(PROPERTY_BASE_URL))
            .client(get("withToken"))
            .build()
    }

    single(API_WITH_TOKEN){
        val retrofit: Retrofit = get("retrofitWithToken")
        retrofit.create<ApiService>(ApiService::class.java)
    }

    single("withoutToken"){
        OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(get())
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

    single(API_WITHOUT_TOKEN){
        val retrofit: Retrofit = get("retrofitWithoutToken")
        retrofit.create<ApiServiceAuthentication>(ApiServiceAuthentication::class.java)
    }
}