package br.com.concrete.tentacle.features.home


import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class HomeFragmentTest: BaseFragmentTest(){

    override fun setupFragment() {
        testFragment = HomeFragment()
    }

    @Test
    fun showErrorMessageAndButtonAgainOnClickReturnSuccess() {
        mockWebServer.enqueue(MockResponse()
            .setBody(getJson("mockjson/errors/error_400.json"))
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
                .setBody(getJson("mockjson/home/load_home_games_success.json"))
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
                .setBody(getJson("mockjson/home/load_home_games_success_empty.json"))
        )

        onView(withId(R.id.recyclerListError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.no_game_in_home)))
        onView(withId(R.id.progressBarList))
    }

    @Test
    fun showRecyclerViewWithItems() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(getJson("mockjson/home/load_home_games_success.json"))
        )

        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun showErrorMessageAndButtonAgain() {
        mockWebServer.enqueue(MockResponse()
            .setBody(getJson("mockjson/errors/error_400.json"))
            .setResponseCode(400))

        onView(withId(R.id.recyclerListError))
        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withText(R.string.load_again)).
            check(matches(isDisplayed()))
        onView(withId(R.id.errorDescription))
            .check(matches(withText(R.string.load_games_error_not_know)))
        onView(withId(R.id.progressBarList))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun showErrorMessageAndButtonAgainOnClickReturnError() {
        mockWebServer.enqueue(MockResponse()
            .setBody(getJson("mockjson/errors/error_400.json"))
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
            .setBody(getJson("mockjson/errors/error_400.json"))
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

}