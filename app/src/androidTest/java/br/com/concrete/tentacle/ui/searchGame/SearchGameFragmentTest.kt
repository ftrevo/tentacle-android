package br.com.concrete.tentacle.ui.searchGame

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.features.HostActivity
import br.com.concrete.tentacle.features.searchGame.SearchGameFragment
import br.com.concrete.tentacle.mock.MockGame
import br.com.concrete.tentacle.rule.RxImmediateSchedulerRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4::class)
class SearchGameFragmentTest {

    @get:Rule
    val mActivityTestRule = ActivityTestRule(HostActivity::class.java)

    /*@get:Rule
    var rxImmediateSchedulerRule = RxImmediateSchedulerRule()*/

    private val mockWebServer = MockWebServer()
    private val searchGameFragment = SearchGameFragment()

    @Before
    fun setUp() {
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
            setSearchText("Unch")

            val response = MockResponse().setResponseCode(200).setBody(MockGame.LIST_GAME_SUCCESS)
            mockWebServer.enqueue(response)

            /*val takeRequest = mockWebServer.takeRequest()

            assertEquals(path, takeRequest.path)*/

            /*mActivityTestRule.mockWebServer.enqueue(MockResponse().setBody(MockGame.LIST_GAME_SUCCESS))
            val takeRequest = mActivityTestRule.mockWebServer.takeRequest()

            assertEquals(path, takeRequest.path)*/
        }
    }

    @Test
    fun test_registerNewGame() {

        search {
            setSearchText("Fortnite")
        }

    }
}