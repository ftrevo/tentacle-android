package br.com.concrete.tentacle.features.lendgame

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseInstrumentedTest
import br.com.concrete.tentacle.extensions.format
import br.com.concrete.tentacle.extensions.getJson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar


@RunWith(AndroidJUnit4::class)
class LendGameActivityTest: BaseInstrumentedTest() {

    @get:Rule
    var activityRule = object : ActivityTestRule<LendGameActivity>(LendGameActivity::class.java) {}

    lateinit var date: String

    @Before
    fun setUpDate() {
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.WEEK_OF_MONTH, 2)
        date = "até ${currentDate.format("dd/MM/yy")}"
    }

    @Before
    fun setupExtras() {
        val intent = Intent()
        intent.putExtra(LendGameActivity.MEDIA_ID_EXTRA, "someId")
        activityRule.launchActivity(intent)
    }

    @Test
    fun testLoadLendSuccessWithoutReservation() {
        setResponse("mockjson/library/loan/lend_response_success.json".getJson(), 200)

        onView(withId(R.id.mediaRegisterImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.tvGameName)).check(matches(withText("Fifa 2015")))
        onView(withId(R.id.tvReservado)).check(matches(withText("Solicitado por")))
        onView(withId(R.id.tvRequestedBy)).check(matches(withText("RWYEG")))
        onView(withId(R.id.tvTempoReservaLabel)).check(matches(withText("Tempo de reserva")))
        onView(withId(R.id.tvTempoReserva)).check(matches(withText("2 Semanas")))
        onView(withId(R.id.tvDate)).check(matches(withText(date)))
        onView(withId(R.id.btLendGame)).check(matches(isDisplayed()))
    }

    @Test
    fun testLoadLendSuccessWithReservation() {
        setResponse("mockjson/library/loan/lend_response_success_with_reservation.json".getJson(), 200)

        onView(withId(R.id.mediaRegisterImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.tvGameName)).check(matches(withText("Fifa 2015")))
        onView(withId(R.id.tvReservado)).check(matches(withText("Reservado por")))
        onView(withId(R.id.tvRequestedBy)).check(matches(withText("RWYEG")))
        onView(withId(R.id.tvTempoReservaLabel)).check(matches(withText("Tempo de reserva")))
        onView(withId(R.id.tvTempoReserva)).check(matches(withText("2 Semanas")))
        onView(withId(R.id.tvDate)).check(matches(withText(date)))
        onView(withId(R.id.btLendGame)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testLoadLendSuccessWithoutReservationClickButton() {
        setResponse("mockjson/library/loan/lend_response_success.json".getJson(), 200)

        onView(withId(R.id.mediaRegisterImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.tvGameName)).check(matches(withText("Fifa 2015")))
        onView(withId(R.id.tvReservado)).check(matches(withText("Solicitado por")))
        onView(withId(R.id.tvRequestedBy)).check(matches(withText("RWYEG")))
        onView(withId(R.id.tvTempoReservaLabel)).check(matches(withText("Tempo de reserva")))
        onView(withId(R.id.tvTempoReserva)).check(matches(withText("2 Semanas")))
        onView(withId(R.id.tvDate)).check(matches(withText(date)))
        onView(withId(R.id.btLendGame)).check(matches(isDisplayed()))

        setResponse("mockjson/library/loan/lend_response_success_updated.json".getJson(), 200)

        onView(withId(R.id.btLendGame)).check(matches(isDisplayed())).perform(click())

        assertTrue(activityRule.activity.isFinishing)
    }

    @Test
    fun testLoadLendSuccessWithoutReservationClickButtonWithoutConnection() {
        setResponse("mockjson/library/loan/lend_response_success.json".getJson(), 200)

        onView(withId(R.id.mediaRegisterImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.tvGameName)).check(matches(withText("Fifa 2015")))
        onView(withId(R.id.tvReservado)).check(matches(withText("Solicitado por")))
        onView(withId(R.id.tvRequestedBy)).check(matches(withText("RWYEG")))
        onView(withId(R.id.tvTempoReservaLabel)).check(matches(withText("Tempo de reserva")))
        onView(withId(R.id.tvTempoReserva)).check(matches(withText("2 Semanas")))
        onView(withId(R.id.tvDate)).check(matches(withText(date)))
        onView(withId(R.id.btLendGame)).check(matches(isDisplayed()))

        setResponse("mockjson/errors/error_400.json".getJson(), 400)

        onView(withId(R.id.btLendGame)).check(matches(isDisplayed())).perform(click())

        onView(withText("ERROR MESSAGE.")).check(matches(isDisplayed()))
    }

    private fun setResponse(json: String, code: Int) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(json)
        )
    }
}