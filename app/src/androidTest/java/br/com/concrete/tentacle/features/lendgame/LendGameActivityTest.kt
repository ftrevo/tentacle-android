package br.com.concrete.tentacle.features.lendgame

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.getJson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LendGameActivityTest {

    @get:Rule
    var activityRule = object : ActivityTestRule<LendGameActivity>(LendGameActivity::class.java) {}

    lateinit var mockWebServer: MockWebServer
    lateinit var testFragment: Fragment

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
        intent.putExtra(LendGameActivity.MEDIA_ID_EXTRA, "someId")
        activityRule.launchActivity(intent)
    }

    @Test
    fun testLoadLendSuccess() {
        setResponse()
        onView(withId(R.id.mediaRegisterImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.tvGameName)).check(matches(withText("Fifa 2015")))
        onView(withId(R.id.tvReservado)).check(matches(withText("Solicitado por")))
        onView(withId(R.id.tvRequestedBy)).check(matches(withText("RWYEG")))
        onView(withId(R.id.tvTempoReservaLabel)).check(matches(withText("Tempo de reserva")))
        onView(withId(R.id.tvTempoReserva)).check(matches(withText("2 Semanas")))
        onView(withId(R.id.tvDate)).check(matches(withText("até 07/03/19")))
        onView(withId(R.id.btLendGame)).check(matches(withText("Entregar Jogo")))
    }

    @Test
    fun testLoadLendClickButtonSuccess() {
        setResponse()
    }

    private fun setResponse() {
        val response = "mockjson/library/loan/lend_response_success.json".getJson()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )
    }
}