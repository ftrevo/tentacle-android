package br.com.concrete.tentacle.features.lendgame

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.waitUntil
import br.com.concrete.tentacle.features.loadmygames.LoadMyGamesFragment
import okhttp3.mockwebserver.MockResponse
import org.junit.Test

class TestDeleteMenuButton : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = LoadMyGamesFragment()
    }

    @Test
    fun testDeleteGameSuccess() {
        setResponse("mockjson/loadmygames/load_my_games_success.json".getJson(), 200)
        setResponse("mockjson/loadmygames/load_my_games_success_second.json".getJson(), 200)
        setResponse("mockjson/library/loan/lend_response_success.json".getJson(), 200)
        setResponse("mockjson/loadmygames/delete_success.json".getJson(), 200)
        setResponse("mockjson/loadmygames/load_my_games_success_after_delete.json".getJson(), 200)
        setResponse("mockjson/loadmygames/load_my_games_success_second.json".getJson(), 200)

        onView(withId(R.id.recyclerListView).childAtPosition(
            0
        ).childAtPosition(0)).check(matches(hasDescendant(withText("TEST"))))
        onView(withId(R.id.recyclerListView).childAtPosition(
            0
        ).childAtPosition(0)).perform(click())

        onView(withId(R.id.delete)).perform(click())

        onView(withId(android.R.id.button1))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.recyclerListView).childAtPosition(
            0
        ).childAtPosition(0)).check(matches(hasDescendant(withText("FIFA 06: Road to FIFA World Cup"))))
    }

    private fun setResponse(json: String, code: Int) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(json)
        )
    }
}