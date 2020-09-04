package br.com.concrete.tentacle.features.filter

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.waitUntil
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not

class filterArrange(action: filterArrange.() -> Unit) {

    init {
        action.invoke(this)
    }

    fun mockResponse(mockWebServer: MockWebServer) {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/get_library_success.json".getJson())
        mockWebServer.enqueue(mockResponse)
    }

}

class filterAct(action: filterAct.() -> Unit) {

    init {
        action.invoke(this)
    }

    fun clickPS3() {
        onView(withText("Playstation 3")).perform(click())
    }

    fun clickPS4() {
        onView(withText("Playstation 4")).perform(click())
    }

    fun clickFilterButton() {
        onView(withId(R.id.filterButtonView)).perform(click())
    }

    fun clickClearButton() {
        onView(withId(R.id.filterClearButtonView)).perform(click())
    }

    fun clickMenu() {
        onView(withId(R.id.filterMenuId)).perform(click())
    }

    fun waitFilterDisplay() {
        onView(withId(R.id.filterContent)).perform(ViewMatchers.isDisplayed().waitUntil())
    }

}

class filterAssert(action: filterAssert.() -> Unit) {

    init {
        action.invoke(this)
    }

    private fun isDisplayed(@StringRes id: Int) {
        onView(withId(id))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun listIsDisplayed() {
        isDisplayed(R.id.list)
    }

    fun clearButtonIsDisplayed() {
        isDisplayed(R.id.filterClearButtonView)
    }

    fun clearButtonIsNotDisplayed() {
        onView(withId(R.id.filterClearButtonView))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    }

}

