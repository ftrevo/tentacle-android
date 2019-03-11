package br.com.concrete.tentacle.features.library

import android.widget.EditText
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.waitUntil
import br.com.concrete.tentacle.matchers.RecyclerViewMatcher.Companion.withRecyclerView
import kotlinx.android.synthetic.main.list_custom.recyclerListView
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers
import org.junit.Test

class LibraryFragmentTest : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = LibraryFragment()
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
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.errorDescription))
            .check(ViewAssertions.matches(withText("Nossa biblioteca ainda não tem\nnenhum jogo cadastrado. Seja o\nprimeiro a cadastrar.")))
        onView(withId(R.id.progressBarList))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun showRecycleViewWithItems() {
        val response = "mockjson/library/get_library_success.json".getJson()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )

        onView(withId(R.id.progressBarList))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun showErrorMessageAndButtonLoadAgain() {
        mockWebServer.enqueue(MockResponse()
            .setBody("mockjson/errors/error_400.json".getJson())
            .setResponseCode(400))

        onView(withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.progressBarList))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.errorDescription))
            .check(ViewAssertions.matches(withText("Ocorreu um erro ao carregar a bilioteca de jogos.\nPor favor, tente novamente.")))
    }

    @Test
    fun showListWhenTypeFourLetters() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/get_library_success.json".getJson()))

        onView(withId(R.id.action_search)).perform(click())

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/get_library_filtered_success.json".getJson()))

        onView(ViewMatchers.isAssignableFrom(EditText::class.java))
            .perform(ViewActions.typeText("FIFA"))

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun showEmptyStateAfterSearch() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/get_library_success.json".getJson()))

        onView(withId(R.id.action_search)).perform(click())

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/common/common_empty_list_success.json".getJson()))

        onView(ViewMatchers.isAssignableFrom(EditText::class.java))
            .perform(ViewActions.typeText("FIFA"))

        onView(withId(R.id.recyclerListError))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.errorDescription))
            .check(ViewAssertions.matches(withText("Ops! A sua busca não retornou nenhum resultado")))
    }

    @Test
    fun showProblemWithConnection() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/get_library_success.json".getJson()))

        onView(withId(R.id.action_search)).perform(click())

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(400)
            .setBody("mockjson/errors/error_400.json".getJson()))

        onView(ViewMatchers.isAssignableFrom(EditText::class.java))
            .perform(ViewActions.typeText("FIFA"))

        onView(withId(R.id.recyclerListError))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.errorDescription))
            .check(ViewAssertions.matches(withText("Ocorreu um erro ao carregar a bilioteca de jogos.\nPor favor, tente novamente.")))
    }

    @Test
    fun afterClickLoadAgainShouldMakeAnotherRequestAndShowList() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/get_library_success.json".getJson()))

        onView(withId(R.id.action_search)).perform(click())

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(400)
            .setBody("mockjson/errors/error_400.json".getJson()))

        onView(ViewMatchers.isAssignableFrom(EditText::class.java))
            .perform(ViewActions.typeText("FIFA"))

        onView(withId(R.id.buttonNameError))
            .perform(isDisplayed().waitUntil())

        Espresso.pressBack()

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/searchgame/list_game_success.json".getJson()))

        onView(withId(R.id.buttonNameError))
            .perform(click())

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun showRecycleViewWithItemsEndLessSuccess() {
        val response = "mockjson/library/get_library_success.json".getJson()
        val responseSecond = "mockjson/library/get_library_success_second.json".getJson()
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
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))

        val oldCount: Int = testFragment.recyclerListView.adapter?.itemCount!!
        onView(withId(R.id.recyclerListView)).perform(scrollToPosition<LibraryViewHolder>(testFragment.recyclerListView.adapter?.itemCount!! - 1))

        Thread.sleep(2500)

        onView(withId(R.id.recyclerListView)).perform(scrollToPosition<LibraryViewHolder>(oldCount))

        onView(withRecyclerView(R.id.recyclerListView).atPosition(oldCount - 1))
            .check(matches(hasDescendant(withText("JOGO FIRST"))))
    }

    @Test
    fun showListStateAfterSearchAndScrollingToEndSuccess() {
        val response = "mockjson/library/get_library_success.json".getJson()
        val responseSecond = "mockjson/library/get_library_success_second.json".getJson()

        mockWebServer.enqueue(
            MockResponse()
            .setResponseCode(200)
            .setBody(response)
        )

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))

        onView(withId(R.id.action_search)).perform(click())

        onView(ViewMatchers.isAssignableFrom(EditText::class.java))
            .perform(ViewActions.typeText("FIFA"))

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

        val oldCount: Int = testFragment.recyclerListView.adapter?.itemCount!!
        onView(withId(R.id.recyclerListView)).perform(scrollToPosition<LibraryViewHolder>(testFragment.recyclerListView.adapter?.itemCount!! - 1))

        onView(withId(R.id.recyclerListView)).perform(scrollToPosition<LibraryViewHolder>(oldCount))

        onView(withRecyclerView(R.id.recyclerListView).atPosition(0))
            .check(matches(hasDescendant(withText("JOGO FIRST"))))
    }
}