package br.com.concrete.tentacle.base

import android.app.Instrumentation
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.concrete.tentacle.di.networkModule
import br.com.concrete.tentacle.di.repositoryModule
import br.com.concrete.tentacle.di.viewModelModule
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Ignore
@RunWith(MockitoJUnitRunner::class)
open class BaseViewModelTest : Instrumentation(), KoinTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var mockServer : MockWebServer

    @Before
    @Throws fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        StandAloneContext.startKoin(
            listOf(
                networkModule,
                repositoryModule,
                viewModelModule
            ))
    }

    @After
    @Throws fun tearDown() {
        StandAloneContext.stopKoin()
        mockServer.shutdown()
    }
}