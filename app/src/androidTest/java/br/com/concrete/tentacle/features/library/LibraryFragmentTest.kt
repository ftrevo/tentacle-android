package br.com.concrete.tentacle.features.library

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers
import org.junit.Test

class LibraryFragmentTest : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = LibraryFragment()
    }

    @Test
    fun showEmptyErrorCustomLayout() {
        val response = getJson("mockjson/library/load_library_empty_list_success.json")

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )

        Espresso.onView(ViewMatchers.withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.errorDescription))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.no_library_error)))
        Espresso.onView(ViewMatchers.withId(R.id.progressBarList))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun showRecycleViewWithItems() {
        val response = getJson("mockjson/library/get_library_success.json")
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )

        Espresso.onView(ViewMatchers.withId(R.id.progressBarList))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun showErrorMessageAndButtonLoadAgain() {
        mockWebServer.enqueue(MockResponse()
            .setBody(getJson("mockjson/errors/error_400.json"))
            .setResponseCode(400))

        Espresso.onView(ViewMatchers.withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.progressBarList))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.errorDescription))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.load_library_error_not_know)))
    }
}