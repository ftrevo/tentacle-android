package br.com.concrete.tentacle.features.library.loan

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.matchers.RecyclerViewMatcher.Companion.withRecyclerView
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoanActivityTest {

    @get:Rule
    var activityRule = object : ActivityTestRule<LoanActivity>(LoanActivity::class.java) {}

    lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    fun after() {
        mockWebServer.shutdown()
    }

    @Before
    fun setupExtras() {
        val intent = Intent()
        intent.putExtra(LoanActivity.ID_LIBRARY_EXTRA, "someId")
        activityRule.launchActivity(intent)
    }

    @Test
    fun testLoadLibraryWithOnePlatformAndOneOwner() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/detail_game_success.json".getJson()))

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/get_one_owner_library.json".getJson()))
        onView(withId(R.id.tvGameName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvGameName)).check(matches(withText("Jogo XPTO")))
        onView(withId(R.id.btPerformLoan)).check(matches(isEnabled()))
    }

    @Test
    fun testLoadLibraryWithPlatforms() {
        setResponse()
        onView(withId(R.id.tvGameName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvGameName)).check(matches(withText("Jogo XPTO")))
        onView(withId(R.id.btPerformLoan)).perform(scrollTo()).check(matches(not(isEnabled())))
    }

    @Test
    fun checkVisibleChips() {
        setResponse()

        onView(withId(R.id.chipPs4)).check(matches(isDisplayed()))
        onView(withId(R.id.chipPs3)).check(matches(not(isDisplayed())))
        onView(withId(R.id.chip360)).check(matches(not(isDisplayed())))
        onView(withId(R.id.chip3ds)).check(matches(isDisplayed()))
        onView(withId(R.id.chipOne)).check(matches(not(isDisplayed())))
        onView(withId(R.id.chipSwitch)).check(matches(not(isDisplayed())))
    }

    @Test
    fun checkButtonState() {
        setResponse()
        onView(withId(R.id.chipPs4)).perform(scrollTo()).perform(click())
        onView(withId(R.id.spOwners)).perform(scrollTo())
        onView(withId(R.id.spOwners)).check(matches(withText("John Doe")))
        onView(withId(R.id.btPerformLoan)).check(matches(isEnabled()))
    }

    @Test
    fun showDetailGame() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/detail_game_success.json".getJson()))

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/get_one_owner_library.json".getJson()))

        checkDetails()

        onView(withId(R.id.btPerformLoan))
            .perform(ViewActions.scrollTo())
        onView(withId(R.id.btPerformLoan))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btPerformLoan))
            .perform(click())

        Assert.assertTrue(activityRule.activity.isFinishing)
    }

    @Test
    fun showDetailAndClickImage() {
        Intents.init()
        checkDetails()

        onView(withRecyclerView(R.id.recyclerView).atPosition(1))
            .perform(click())

        intended(hasComponent(PinchToZoomActivity::class.java.name))
        Intents.release()
    }

    private fun checkDetails() {
        setResponse()

        val responseJson =
            "mockjson/library/detail_game_success.json".getJson()

        val mediaReservationSuccess =
            "mockjson/registerMedia/register_media_success.json".getJson()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mediaReservationSuccess)
        )

        onView(withId(R.id.tvGameName))
            .check(matches(withText("Jogo XPTO")))
        onView(withId(R.id.tvGameSummary))
            .check(matches(withText("Lead your country to World Cup glory while experiencing all the fun, excitement, and drama of Football's greatest event.")))
        onView(withId(R.id.tvGameReleaseYear))
        onView(withText("Sport")).check(matches(ViewMatchers.isDisplayed()))
        onView(withText("Single player")).check(matches(ViewMatchers.isDisplayed()))
        onView(withText("Multiplayer")).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkButtonStateError426() {
        setResponse()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(426)
                .setBody("mockjson/errors/error_400.json".getJson())
        )
        onView(withId(R.id.chipPs4)).perform(scrollTo()).perform(click())
        onView(withId(R.id.spOwners)).perform(scrollTo())
        onView(withId(R.id.spOwners)).check(matches(withText("John Doe")))
        onView(withId(R.id.btPerformLoan)).check(matches(isEnabled())).perform(scrollTo()).perform(click())

        onView(withText("ERROR MESSAGE.")).check(matches(isDisplayed()))
    }

    private fun setResponse() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/detail_game_success.json".getJson()))

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/get_one_id_library.json".getJson()))
    }
}