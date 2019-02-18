package br.com.concrete.tentacle.features.library.filter

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.waitUntil
import br.com.concrete.tentacle.features.library.LibraryFragment
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

class FilterDialogFragmentTest: BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = LibraryFragment()
    }

    @Before
    fun setFragment(){
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("mockjson/library/get_library_success.json".getJson()))
        onView(withId(R.id.filterMenuId)).perform(click())
    }

    @Test
    fun btnClearFilterIsDisplayed(){
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("mockjson/library/get_library_success.json".getJson()))
        onView(
            withId(R.id.filterContent)
        ).perform(isDisplayed().waitUntil())

        onView(
            allOf(
                withText("Playstation 3")
                ))
            .perform(click())

        onView(withId(R.id.filterClearButtonView))
            .check(matches(isDisplayed()))

    }

    @Test
    fun btnClearFilterDisappear(){
        onView(
            withId(R.id.filterContent)
        ).perform(isDisplayed().waitUntil())

        onView(
            allOf(
                withText("Playstation 3")
            ))
            .perform(click())

        onView(withId(R.id.filterClearButtonView))
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withText("Playstation 3")
            ))
            .perform(click())

        onView(withId(R.id.filterClearButtonView))
            .check(matches(not(isDisplayed())))

    }


    @Test
    fun btnClearFilterSuccess(){
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("mockjson/library/get_library_success.json".getJson()))
        onView(
            withId(R.id.filterContent)
        ).perform(isDisplayed().waitUntil())

        onView(
            allOf(
                withText("Playstation 3")
            ))
            .perform(click())

        onView(
            allOf(
                withText("Playstation 4")
            ))
            .perform(click())

        onView(withId(R.id.filterClearButtonView)).perform(click())
        onView(withId(R.id.list)).check(matches(isDisplayed()))
    }

    @Test
    fun btnFilterSuccess(){
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("mockjson/library/get_library_success.json".getJson()))
        onView(
            withId(R.id.filterContent)
        ).perform(isDisplayed().waitUntil())

        onView(
            allOf(
                withText("Playstation 3")
            ))
            .perform(click())

        onView(
            allOf(
                withText("Playstation 4")
            ))
            .perform(click())

        onView(withId(R.id.filterButtonView)).perform(click())
        onView(withId(R.id.list)).check(matches(isDisplayed()))
    }

}