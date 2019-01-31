package br.com.concrete.tentacle.features.registerGame.searchGame

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.base.BaseSearchFragment
import br.com.concrete.tentacle.data.models.ErrorResponse
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers
import org.junit.Test


class SearchGameViewModelTest: BaseFragmentTest(){
    override fun setupFragment() {
        testFragment = SearchGameFragment()
        (testFragment as BaseSearchFragment)
    }

    @Test
    fun showListWhenTypeFourLetters(){
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(getJson("mockjson/searchgame/list_game_success.json")))

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("FIFA"))

        onView(withId(R.id.progressBarList))
            .perform(waitUntil(isDisplayed()))
        onView(withId(R.id.progressBarList))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .perform(waitUntil(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun showButtonWhenListIsEmpty(){
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(getJson("mockjson/searchgame/list_game_empty_success.json")))

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("FIFA"))

        onView(withId(R.id.progressBarList))
            .perform(waitUntil(isDisplayed()))
        onView(withId(R.id.progressBarList))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.recyclerListError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.first_game_register)))
    }

    @Test
    fun showProblemWithConection(){
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(400)
            .setBody(getJson("mockjson/errors/error_400.json")))

        val error = GsonBuilder().create().fromJson<ErrorResponse>(getJson("mockjson/errors/error_400.json"), ErrorResponse::class.java)

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("FIFA"))

        onView(withId(R.id.progressBarList))
            .perform(waitUntil(isDisplayed()))
        onView(withId(R.id.progressBarList))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.recyclerListError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.load_games_error_not_know)))
    }

}