package br.com.concrete.tentacle.ui.searchGame

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.features.TestActivity
import br.com.concrete.tentacle.features.searchGame.SearchGameFragment
import br.com.concrete.tentacle.rule.KoinActivityTestRule
import br.com.concrete.tentacle.testing.SingleFragmentActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4::class)
class SearchGameFragmentTest : KoinTest {

    @get:Rule
    val mActivityTestRule = KoinActivityTestRule(SingleFragmentActivity::class.java)

    /*@get:Rule
    val mActivityTestRule = ActivityTestRule(TestActivity::class.java)*/

    private val searchGameFragment = SearchGameFragment()

    @Test
    fun test() {
        search {

            val path = "/games"
            clickSearchButton()
            setSearchText("FIFA")


            /*mActivityTestRule.mockWebServer.enqueue(MockResponse().setBody(MockGame.LIST_GAME_SUCCESS))
            val takeRequest = mActivityTestRule.mockWebServer.takeRequest()

            assertEquals(path, takeRequest.path)*/
        }
    }
}