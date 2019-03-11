package br.com.concrete.tentacle.features.myreservations.detail

import android.content.Intent
import androidx.test.espresso.Espresso.onView
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

class MyReservationDetaillActivityTest : BaseInstrumentedTest() {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<MyReservationActivity>(MyReservationActivity::class.java) {}

    @Before
    fun setupExtras() {
        val intent = Intent()
        intent.putExtra(MyReservationActivity.LOAN_EXTRA_ID, "someId")
        activityTestRule.launchActivity(intent)
    }

    @Test
    fun showActiveLoan() {

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/myreservations/detail/load_active_reservation.json".getJson())
        )
        verifyFilds()
        onView(withId(R.id.tvGameStatus))
            .check(matches(withText("Faltam 4 dias")))
    }

    @Test
    fun showPendingLoan() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/myreservations/detail/load_pending_reservation.json".getJson())
        )
        verifyFilds()
        onView(withId(R.id.tvGameStatus))
            .check(matches(withText("Pendente")))
    }

    @Test
    fun showExpiredLoan() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/myreservations/detail/load_expired_reservation.json".getJson())
        )
        verifyFilds()
        onView(withId(R.id.tvGameStatus))
            .check(matches(withText("Expirado")))
    }

    private fun verifyFilds() {
        onView(withId(R.id.tvGameName))
            .check(matches(withText("The Last of Us Remastered")))

        onView(withId(R.id.tvGameReleaseYear))
            .check(matches(withText("2014")))

        onView(withId(R.id.tvGameSummary))
            .check(matches((4.checkLines())))

        onView(withText("Single player"))
            .check(matches(isDisplayed()))

        onView(withText("Multiplayer"))
            .check(matches(isDisplayed()))

        onView(withText("Shooter"))
            .check(matches(isDisplayed()))

        onView(withText("Adventure"))
            .check(matches(isDisplayed()))

        onView(withText("Multiplayer"))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvGamePlatform))
            .check(matches(withText("PS4")))

        onView(withId(R.id.tvGameOwner))
            .check(matches(withText("FELIPE TREVISAN")))
    }
}