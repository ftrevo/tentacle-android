package br.com.concrete.tentacle.features.my_reservations.detail

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseInstrumentedTest
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.features.myreservations.detail.MyReservationActivity
import br.com.concrete.tentacle.matchers.IsTextInLines
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyReservationDetaillActivityTest : BaseInstrumentedTest() {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<MyReservationActivity>(MyReservationActivity::class.java) {}

    @Before
    fun setupExtras() {
        val intent = Intent()
        intent.putExtra(MyReservationActivity.LOAN_EXTRA_ID, "someId")
        activityTestRule.launchActivity(intent)
    }

    @Before
    fun setMock() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/myreservations/detail/get_my_reservation_detail.json".getJson())
        )
    }

    @Test
    fun showActiveLoan() {
        onView(withId(R.id.tvGameName))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvGameName))
            .check(matches(withText("The Last of Us Remastered")))

        onView(withId(R.id.tvGameReleaseYear))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvGameReleaseYear))
            .check(matches(withText("2014")))

        onView(withId(R.id.tvGameStatus))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvGameStatus))
            .check(matches(withText("Faltam 5 dias")))

        onView(withId(R.id.ivGameStatus))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvGameSummary))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvGameSummary))
            .check(matches((IsTextInLines(4))))

        Espresso.onView(withText("Single player"))
            .check(matches(isDisplayed()))

        Espresso.onView(withText("Multiplayer"))
            .check(matches(isDisplayed()))

        Espresso.onView(withText("Shooter"))
            .check(matches(isDisplayed()))

        Espresso.onView(withText("Adventure"))
            .check(matches(isDisplayed()))

        Espresso.onView(withText("Multiplayer"))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.tvGamePlatform))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.tvGamePlatform))
            .check(matches(withText("PS4")))

        Espresso.onView(withId(R.id.tvGameOwner))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.tvGameOwner))
            .check(matches(withText("FELIPE TREVISAN")))

        Espresso.onView(withId(R.id.tvLoanInfo))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun showPendingLoan() {
        // TODO
    }

    @Test
    fun showExpiredLoan() {
        // TODO
    }
}