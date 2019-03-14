package br.com.concrete.tentacle.features.lendgame

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.features.loadmygames.LoadMyGamesFragment
import okhttp3.mockwebserver.MockResponse
import org.junit.Test

class TestDeleteMenuButton: BaseFragmentTest(){

    override fun setupFragment() {
        testFragment = LoadMyGamesFragment()
    }

    @Test
    fun testDeleteGameSuccess(){
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
        onView(withText("EXCLUIR")).perform(click())
        onView(withId(R.id.recyclerListView).childAtPosition(
            0
        ).childAtPosition(0)).check(matches(hasDescendant(withText("FIFA 06: Road to FIFA World Cup"))))


    }

    @Test
    fun testDeleteGameError(){
        setResponse("mockjson/loadmygames/load_my_games_success.json".getJson(), 200)
        setResponse("mockjson/loadmygames/load_my_games_success_second.json".getJson(), 200)
        setResponse("mockjson/library/loan/lend_response_success.json".getJson(), 200)
        setResponse("mockjson/errors/error_400.json".getJson(), 400)

        onView(withText("TEST")).perform(click())
        onView(withId(R.id.delete)).perform(click())
        onView(withText("EXCLUIR")).perform(click())
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