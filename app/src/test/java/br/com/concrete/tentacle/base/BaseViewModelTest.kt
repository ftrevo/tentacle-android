package br.com.concrete.tentacle.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.concrete.tentacle.di.PROPERTY_BASE_URL
import br.com.concrete.tentacle.di.mockAndroidModule
import br.com.concrete.tentacle.di.networkModule
import br.com.concrete.tentacle.di.refreshTokenRequestTestModule
import br.com.concrete.tentacle.di.repositoryModule
import br.com.concrete.tentacle.di.viewModelModule
import br.com.concrete.tentacle.rules.RxImmediateSchedulerRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.KoinProperties
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest
import java.net.HttpURLConnection

open class BaseViewModelTest : KoinTest {

    var mockServer: MockWebServer = MockWebServer()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var rxRule = RxImmediateSchedulerRule()

    @Before
    @Throws fun setUp() {

        StandAloneContext.startKoin(
            listOf(
                networkModule,
                viewModelModule,
                repositoryModule,
                mockAndroidModule,
                refreshTokenRequestTestModule
            ), properties = KoinProperties(extraProperties = mapOf(PROPERTY_BASE_URL to "http://localhost:8080/"))
        )

        mockServer.start(8080)
    }

    @After
    @Throws fun tearDown() {
        StandAloneContext.stopKoin()
        mockServer.shutdown()
    }

    fun getJson(path: String): String? {
        return this::class
            .java
            .classLoader?.getResource(
            path
        )?.readText()
    }

    fun mockResponseError400() {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
            .setBody(getJson("mockjson/errors/error_400.json"))
        mockServer.enqueue(mockResponse)
    }

    fun mockResponse200(responseJson: String?) {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)
        mockServer.enqueue(mockResponse)
    }

    fun mockResponse201(responseJson: String?) {
        val mockResponse = MockResponse()
            .setResponseCode(201)
            .setBody(responseJson)
        mockServer.enqueue(mockResponse)
    }

    fun mockResponseError404() {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
            .setBody(getJson("mockjson/errors/error_404.json"))
        mockServer.enqueue(mockResponse)
    }

    fun mockResponseError401() {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
            .setBody(getJson("mockjson/errors/error_401.json"))
        mockServer.enqueue(mockResponse)
    }
}