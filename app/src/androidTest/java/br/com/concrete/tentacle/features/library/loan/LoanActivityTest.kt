package br.com.concrete.tentacle.features.library.loan

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.features.library.LoanActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoanActivityTest {

    @get:Rule
    var activityRule = object : ActivityTestRule<LoanActivity>(LoanActivity::class.java) {}

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
    fun setupExtras(){
        val intent = Intent()
        intent.putExtra(LoanActivity.ID_LIBRARY_EXTRA, "someId")
        activityRule.launchActivity(intent)
    }

    @Test
    fun testLoadLibrary(){
        setResponse()

        onView(withId(R.id.tvGameName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvGameName)).check(matches(withText("Jogo XPTO")))
        onView(withId(R.id.btPerformLoan)).check(matches(not(isEnabled())))

    }

    @Test
    fun checkVisibleChips(){
        setResponse()

        onView(withId(R.id.chipPs4)).check(matches(isDisplayed()))
        onView(withId(R.id.chipPs3)).check(matches(not(isDisplayed())))
        onView(withId(R.id.chip360)).check(matches(not(isDisplayed())))
        onView(withId(R.id.chip3ds)).check(matches(not(isDisplayed())))
        onView(withId(R.id.chipOne)).check(matches(not(isDisplayed())))
        onView(withId(R.id.chipSwitch)).check(matches(not(isDisplayed())))

    }

    @Test
    fun checkButtonState(){

        setResponse()

        onView(withId(R.id.chipPs4)).perform(click())
        onView(withId(R.id.spOwners)).perform(click())
        onView(withId(R.id.tv_tinted_spinner)).perform(click()) //Clicking on the first item on list
        onView(withId(R.id.spOwners)).check(matches(withText("John Doe")))
        onView(withId(R.id.btPerformLoan)).check(matches(isEnabled()))
    }

    private fun setResponse(){
        val response = "mockjson/library/loan/library_response_success.json".getJson()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )
    }
}