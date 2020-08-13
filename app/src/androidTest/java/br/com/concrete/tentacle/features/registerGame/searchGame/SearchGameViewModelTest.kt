package br.com.concrete.tentacle.features.registerGame.searchGame

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.base.BaseSearchFragment
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.waitUntil
import br.com.concrete.tentacle.matchers.RecyclerViewMatcher.Companion.withRecyclerView
import kotlinx.android.synthetic.main.list_custom.*
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers
import org.junit.Test

class SearchGameViewModelTest : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = SearchGameFragment()
        (testFragment as BaseSearchFragment)
    }

    @Test
    fun showListWhenTypeFourLetters() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/searchgame/list_game_success.json".getJson()))

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("FIFA"))

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListError))
            .check(matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun showProgressBar() {
        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("FIFA"))

        onView(withId(R.id.recyclerListError))
            .check(matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.recyclerListView))
            .check(matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun showButtonWhenListIsEmpty() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/searchgame/list_game_empty_success.json".getJson()))

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("FIFA"))

        onView(withId(R.id.recyclerListError))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.first_game_register)))
    }

    @Test
    fun showProblemWithConnection() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(400)
            .setBody("mockjson/errors/error_400.json".getJson()))

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("FIFA"))

        onView(withId(R.id.recyclerListError))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.load_games_error_not_know)))
    }

    @Test
    fun afterClickLoadAgainShouldMakeAnotherRequestAndShowList() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(400)
            .setBody("mockjson/errors/error_400.json".getJson()))

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("FIFA"))

        onView(withId(R.id.buttonNameError))
            .perform(isDisplayed().waitUntil())

        pressBack()

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/searchgame/list_game_success.json".getJson()))

        onView(withId(R.id.buttonNameError))
            .perform(click())

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListError))
            .check(matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun afterSearchAndVerifyItemIsOk() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/searchgame/list_game_success.json".getJson()))

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("FIFA"))

        pressBack()

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())

        onView(withRecyclerView(R.id.recyclerListView).atPosition(0))
            .check(matches(ViewMatchers.hasDescendant(withText("Jogo 01"))))
    }

    @Test
    fun afterSearchAndScrollingToEndSuccess() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/searchgame/list_game_success.json".getJson()))
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/searchgame/list_game_success_second.json".getJson()))

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("jo"))

        pressBack()

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))

        val oldCount: Int = testFragment.recyclerListView.adapter?.itemCount!!
        onView(withId(R.id.recyclerListView)).perform(
            RecyclerViewActions.scrollToPosition<SearchGameViewHolder>(
                testFragment.recyclerListView.adapter?.itemCount!! - 1
            )
        )

        onView(withId(R.id.recyclerListView)).perform(RecyclerViewActions.scrollToPosition<SearchGameViewHolder>(oldCount))

        Thread.sleep(2600)

        onView(withRecyclerView(R.id.recyclerListView).atPosition(oldCount))
            .check(matches(ViewMatchers.hasDescendant(withText("JOGO FIRST"))))
    }
}