package br.com.concrete.tentacle.features.menu

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.getJson
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Test

class MenuFragmentTest : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = MenuFragment()
    }

    @Before
    fun setUpMenu() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/profile/get_profile_success.json".getJson())
        )
    }

    @Test
    fun checkFields() {
        onView(withId(R.id.name)).check(matches(withText("Damiana dos toró")))
        onView(withId(R.id.state)).check(matches(withText("Pernambuco")))
    }

    @Test
    fun checkLogout() {
        onView(withId(R.id.logout)).perform(click())
        onView(withText("OK")).perform(click())
        assert(activityRule.activity.isFinishing)
    }

    @Test
    fun checkCancelLogout() {
        onView(withId(R.id.logout)).perform(click())
        onView(withText("CANCELAR")).perform(click())
        onView(withId(R.id.name)).check(matches(withText("Damiana dos toró")))
        onView(withId(R.id.state)).check(matches(withText("Pernambuco")))
    }
}