package br.com.concrete.tentacle.base

import android.app.Application
import android.app.Instrumentation
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.concrete.tentacle.data.network.ApiServiceAuthentication
import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.di.*
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.android.ext.koin.with
import org.koin.core.KoinProperties
import org.koin.standalone.StandAloneContext
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.test.KoinTest
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@Ignore
@RunWith(MockitoJUnitRunner::class)
open class BaseViewModelTest : Instrumentation(), KoinTest {

    lateinit var mockServer : MockWebServer

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        startKoin(
            listOf(
                networkModule,
                repositoryModule,
                sharedPreferencesModule,
                viewModelModule
            ), properties =
            KoinProperties(
                extraProperties = mapOf(PROPERTY_BASE_URL to mockServer.url("/").toString()))
            ) with mock(Application::class.java)
    }

    @After
    @Throws fun tearDown() {
        StandAloneContext.stopKoin()
        mockServer.shutdown()
    }

}