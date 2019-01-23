package br.com.concrete.tentacle.ui.searchGame

import java.util.concurrent.TimeUnit
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.features.HostActivity
import br.com.concrete.tentacle.features.searchGame.SearchGameFragment
import br.com.concrete.tentacle.features.searchGame.SearchGameViewModel
import br.com.concrete.tentacle.mock.MockGame
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.inject
import org.koin.test.KoinTest

typealias TimeHandler = (defaultScheduler: Scheduler, tag: String) -> Scheduler

@RunWith(AndroidJUnit4::class)
class SearchGameFragmentTest : KoinTest {

    @get:Rule
    val mActivityTestRule = ActivityTestRule(HostActivity::class.java)

    val searchGameViewModel: SearchGameViewModel by inject()

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

            val response = MockResponse().setResponseCode(200).setBody(MockGame.LIST_GAME_SUCCESS)
            mockWebServer.enqueue(response)

            setSearchText("Fifa")

            /*val scheduler = TestScheduler()
            TimeScheduler.timeSchedulerHandler = { default, tag ->
                if (tag == "click_debounce") scheduler else default
            }

            scheduler.advanceTimeBy(5, TimeUnit.MINUTES)*/
            isDisplayedRecyclerView()
            //waitForMatcher(10000)
            /*isDisplayedRecyclerView()*/
        }
    }


    object TimeScheduler {
        @Volatile var timeSchedulerHandler: TimeHandler? = null

        fun time(tag: String): Scheduler {
            val handler = timeSchedulerHandler ?: return Schedulers.computation()
            return handler(Schedulers.computation(), tag)
        }
    }

    @Test
    fun test_registerNewGame() {

        search {
            setSearchText("Fortnite")
        }

    }


}