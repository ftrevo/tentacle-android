package br.com.concrete.tentacle.features.filter

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.waitUntil
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not

class filterArrange(action: filterArrange.() -> Unit) {

    init {
        action.invoke(this)
    }

    fun mockResponse(mockWebServer: MockWebServer){
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("mockjson/library/get_library_success.json".getJson())
        mockWebServer.enqueue(mockResponse)
    }

    fun waitToDisplay(@StringRes id: Int){
        onView(withId(id)).perform(ViewMatchers.isDisplayed().waitUntil())
    }

    fun click(text: String){
        onView(withText(text)).perform(ViewActions.click())
    }

    fun click(@StringRes id: Int){
        onView(withId(id)).perform(ViewActions.click())
    }

}

class filterAct(action: filterAct.() -> Unit) {

    init {
        action.invoke(this)
    }

    fun click(text: String){
        onView(withText(text)).perform(ViewActions.click())
    }

    fun click(@StringRes id: Int){
        onView(withId(id)).perform(ViewActions.click())
    }

}

class filterAssert(action: filterAssert.() -> Unit) {

    init {
        action.invoke(this)
    }

    fun isDisplayed(@StringRes id: Int) {
        onView(withId(id))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun isNotDisplayed(@StringRes id: Int){
        onView(withId(id))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    }

}

