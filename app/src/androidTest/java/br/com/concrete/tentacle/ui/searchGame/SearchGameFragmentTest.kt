package br.com.concrete.tentacle.ui.searchGame

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.features.HostActivity
import br.com.concrete.tentacle.features.searchGame.SearchGameFragment
import br.com.concrete.tentacle.mock.MockGame
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchGameFragmentTest {

    @get:Rule
    val mActivityTestRule = ActivityTestRule(HostActivity::class.java)

    private val mockWebServer = MockWebServer()
    private val searchGameFragment = SearchGameFragment()

    @Before
    fun setUp() {
        /*startKoin(listOf(networkModule,
                viewModelModule,
                repositoryModule,
                sharedPreferencesModule
            ), KoinProperties(extraProperties = mapOf(PROPERTY_BASE_URL to "http://localhost:8080")))*/

        mockWebServer.start(8080)
    }

    @After
    fun finish() {
        mockWebServer.shutdown()
    }

    @Test
    fun test() {
        search {

            val path = "/games"
            clickSearchButton()
            setSearchText("FIFA")

            val response = MockResponse().setResponseCode(200).setBody(MockGame.LIST_GAME_SUCCESS)

            mockWebServer.enqueue(response)
            val takeRequest = mockWebServer.takeRequest()

            assertEquals(path, takeRequest.path)

            /*mActivityTestRule.mockWebServer.enqueue(MockResponse().setBody(MockGame.LIST_GAME_SUCCESS))
            val takeRequest = mActivityTestRule.mockWebServer.takeRequest()

            assertEquals(path, takeRequest.path)*/
        }
    }
}