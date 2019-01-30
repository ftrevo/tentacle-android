package br.com.concrete.tentacle.base

import PROPERTY_BASE_URL
import br.com.concrete.tentacle.rules.RxImmediateSchedulerRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import java.net.HttpURLConnection

@Ignore
@RunWith(RobolectricTestRunner::class)
open class BaseViewModelTest : KoinTest {

    val mockServer: MockWebServer = MockWebServer()

    @get:Rule
    var rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    @Before
    @Throws fun setUp() {
        mockServer.start()
        getKoin().setProperty(PROPERTY_BASE_URL, mockServer.url("/").toString())
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
}