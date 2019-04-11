package br.com.concrete.tentacle.features.loadmygames

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.waitUntil
import br.com.concrete.tentacle.matchers.RecyclerViewMatcher.Companion.withRecyclerView
import kotlinx.android.synthetic.main.list_custom.recyclerListView
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class LoadMyGamesFragmentTest : BaseFragmentTest() {
    override fun setupFragment() {
        testFragment = LoadMyGamesFragment()
    }

    @Test
    fun showEmptyErrorCustomLayout() {
        val response = "mockjson/common/common_empty_list_success.json".getJson()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )

        onView(withId(R.id.recyclerListError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.no_game_registered)))
        onView(withId(R.id.progressBarList))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun showRecycleViewWithItems() {
        val response = "mockjson/loadmygames/load_my_games_success.json".getJson()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun showErrorMessageAndButtonLoadAgain() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody("mockjson/errors/error_400.json".getJson())
                .setResponseCode(400)
        )

        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.progressBarList))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerListError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.load_games_error_not_know)))
    }

    @Test
    fun showRecycleViewWithItemsEndLessSuccess() {
        val response = "mockjson/loadmygames/load_my_games_success.json".getJson()
        val responseSecond = "mockjson/loadmygames/load_my_games_success_second.json".getJson()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseSecond)
        )

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(not(isDisplayed())))

        val oldCount: Int = testFragment.recyclerListView.adapter?.itemCount!! - 1
        onView(withId(R.id.recyclerListView)).perform(scrollToPosition<LoadMyGamesViewHolder>(testFragment.recyclerListView.adapter?.itemCount!! - 1))

        onView(withId(R.id.recyclerListView)).perform(scrollToPosition<LoadMyGamesViewHolder>(testFragment.recyclerListView.adapter?.itemCount!! - 1))

        Thread.sleep(4000)

        onView(withRecyclerView(R.id.recyclerListView).atPosition(oldCount))
            .check(matches(hasDescendant(withText("SECOND"))))
    }

    @Test
    fun showRecycleViewWithItemsAndLongClick() {
        val response = "mockjson/loadmygames/load_my_games_success.json".getJson()
        val removeGame = "mockjson/loadmygames/load_my_games_remove_success.json".getJson()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(removeGame)
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
        )

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())

        onView(
            withText("TEST"))
            .perform(isDisplayed().waitUntil())
            .check(matches(isDisplayed()))
            .perform(longClick())

        onView(withText("O jogo TEST sairá da lista de \"Meus Jogos\", mas você poderá adicioná-lo novamente no futuro."))

        onView(withText("EXCLUIR"))
            .check(matches(isDisplayed()))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(ViewActions.click())

        onView(withRecyclerView(R.id.recyclerListView)
            .atPosition(0))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(not(withText("TEST")))))
            .check(matches(hasDescendant(not(withText("PS4")))))
    }

    @Test
    fun showEmptyErrorError426() {
        val response = "mockjson/errors/error_400.json".getJson()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(426)
                .setBody(response)
        )

        onView(withText("ERROR MESSAGE.")).check(matches(isDisplayed()))
    }
}
