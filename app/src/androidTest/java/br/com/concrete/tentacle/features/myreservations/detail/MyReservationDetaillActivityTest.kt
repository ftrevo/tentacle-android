package br.com.concrete.tentacle.features.myreservations.detail

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseInstrumentedTest
import br.com.concrete.tentacle.extensions.checkLines
import br.com.concrete.tentacle.extensions.getJson
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class MyReservationDetaillActivityTest : BaseInstrumentedTest() {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<MyReservationActivity>(MyReservationActivity::class.java) {}

    @Before
    fun setupExtras() {
        val intent = Intent()
        intent.putExtra(MyReservationActivity.LOAN_EXTRA_ID, "someId")
        activityTestRule.launchActivity(intent)
    }

    fun getDaysAfter(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysAgo)
        return calendar.time
    }

    @Test
    fun showActiveLoan() {
        val date = getDaysAfter(5)
        var json = "mockjson/myreservations/detail/load_active_reservation.json"
            .getJson()
            .replace(
                "2019-03-06T18:44:58.540Z",
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .format(date)
            )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(json)
        )

        onView(withId(R.id.tvGameStatus))
            .check(matches(withText("Faltam 4 dias")))
        verifyFilds()
    }

    @Test
    fun showPendingLoan() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/myreservations/detail/load_pending_reservation.json".getJson())
        )

        onView(withId(R.id.tvGameStatus))
            .check(matches(withText("Pendente")))
        verifyFilds()
    }

    @Test
    fun showExpiredLoan() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/myreservations/detail/load_expired_reservation.json".getJson())
        )

        onView(withId(R.id.tvGameStatus))
            .check(matches(withText("Expirado")))
        verifyFilds()
    }

    private fun verifyFilds() {
        onView(withId(R.id.tvGameName))
            .check(matches(withText("The Last of Us Remastered")))

        onView(withId(R.id.tvGameReleaseYear))
            .check(matches(withText("2014")))

        onView(withText("Single player"))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withText("Multiplayer"))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withText("Shooter"))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withText("Adventure"))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withText("Multiplayer"))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvGamePlatform))
            .perform(scrollTo())
            .check(matches(withText("PS4")))

        onView(withId(R.id.tvGameOwner))
            .perform(scrollTo())
            .check(matches(withText("FELIPE TREVISAN")))
    }
}