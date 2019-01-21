package br.com.concrete.tentacle.base

import br.com.concrete.tentacle.di.PROPERTY_BASE_URL
import br.com.concrete.tentacle.rules.RxImmediateSchedulerRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner

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
}