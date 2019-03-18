package br.com.concrete.tentacle.features.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.matchers.RecyclerViewMatcher
import br.com.concrete.tentacle.matchers.RecyclerViewMatcher.Companion.withRecyclerView
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class HomeFragmentTest : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = HomeFragment()
    }

    @Test
    fun showErrorMessageAndButtonAgainOnClickReturnSuccess() {
        mockWebServer.enqueue(MockResponse()
            .setBody("mockjson/errors/error_400.json".getJson())
            .setResponseCode(400))

        onView(withId(R.id.recyclerListError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withText(R.string.load_again))
            .check(matches(isDisplayed()))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.load_games_error_not_know)))
        onView(withId(R.id.progressBarList))
            .check(matches(not(isDisplayed())))

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/home/new_home_games_success.json".getJson())
        )

        onView(withId(R.id.buttonNameError))
            .perform(click())
        onView(withId(R.id.progressBarList))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun showEmptyCustomLayout() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/home/load_home_games_success_empty.json".getJson())
        )

        onView(withId(R.id.recyclerListError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.errorDescription))
            .check(matches(withText("A Home ainda não possui jogos novos cadastrados. Comece a cadastrar para eles aparecerem aqui!")))
        onView(withId(R.id.progressBarList))
    }

    @Test
    fun showRecyclerViewWithItems() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/home/new_home_games_success.json".getJson())
        )

        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(not(isDisplayed())))

        onView(RecyclerViewMatcher.withRecyclerView(R.id.recyclerListView).atPosition(0))
            .check(matches(hasDescendant(withText("The Last of Us Remastered"))))
            .check(matches(hasDescendant(allOf(withText("PS4"), isDisplayed()))))
            .check(matches(hasDescendant(allOf(withText("PS3"), not(isDisplayed())))))
            .check(matches(hasDescendant(allOf(withText("360"), not(isDisplayed())))))
            .check(matches(hasDescendant(allOf(withText("ONE"), not(isDisplayed())))))
            .check(matches(hasDescendant(allOf(withText("3DS"), not(isDisplayed())))))
            .check(matches(hasDescendant(allOf(withText("NS"), not(isDisplayed())))))

        onView(RecyclerViewMatcher.withRecyclerView(R.id.recyclerListView).atPosition(1))
            .check(matches(hasDescendant(withText("God of War III"))))
            .check(matches(hasDescendant(allOf(withText("PS4"), not(isDisplayed())))))
            .check(matches(hasDescendant(allOf(withText("PS3"), not(isDisplayed())))))
            .check(matches(hasDescendant(allOf(withText("360"), isDisplayed()))))
            .check(matches(hasDescendant(allOf(withText("ONE"), isDisplayed()))))
            .check(matches(hasDescendant(allOf(withText("3DS"), not(isDisplayed())))))
            .check(matches(hasDescendant(allOf(withText("NS"), not(isDisplayed())))))
    }

    @Test
    fun showErrorMessageAndButtonAgain() {
        mockWebServer.enqueue(MockResponse()
            .setBody("mockjson/errors/error_400.json".getJson())
            .setResponseCode(400))

        onView(withId(R.id.recyclerListError))
        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withText(R.string.load_again))
            .check(matches(isDisplayed()))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.load_games_error_not_know)))
        onView(withId(R.id.progressBarList))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun showErrorMessageAndButtonAgainOnClickReturnError() {
        mockWebServer.enqueue(MockResponse()
            .setBody("mockjson/errors/error_400.json".getJson())
            .setResponseCode(400))

        onView(withId(R.id.recyclerListError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withText(R.string.load_again))
            .check(matches(isDisplayed()))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.load_games_error_not_know)))
        onView(withId(R.id.progressBarList))
            .check(matches(not(isDisplayed())))

        mockWebServer.enqueue(MockResponse()
            .setBody("mockjson/errors/error_400.json".getJson())
            .setResponseCode(400))

        onView(withId(R.id.buttonNameError))
            .perform(click())

        onView(withId(R.id.recyclerListError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withText(R.string.load_again))
            .check(matches(isDisplayed()))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.load_games_error_not_know)))
        onView(withId(R.id.progressBarList))
            .check(matches(not(isDisplayed())))
    }

    /**
     * For more usages of RecyclerViewMatcher check the link below:
     * https://spin.atomicobject.com/2016/04/15/espresso-testing-recyclerviews/
     */
    @Test
    fun shouldShowItemsContentOnRequestSuccess() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/home/new_home_games_success.json".getJson())
        )

        onView(withRecyclerView(R.id.recyclerListView).atPosition(1))
            .check(matches(hasDescendant(withText("God of War III"))))
            .check(matches(hasDescendant(withText("Winner of over 200 game of the year awards"))))
    }
}