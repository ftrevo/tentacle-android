package br.com.concrete.tentacle.features.filter

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
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

class FilterDialogFragmentTest : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = LibraryFragment()
    }

    @Before
    fun setFragment() {
        mockResponse200("mockjson/library/get_library_success.json".getJson())
        onView(withId(R.id.filterMenuId)).perform(click())
    }

    @Test
    fun givenFilter_whenListItemClicked_shouldDisplayClearButton() {
        // arrange
        onView(withId(R.id.filterContent)).perform(isDisplayed().waitUntil())

        // act
        onView(withText("Playstation 3")).perform(click())

        // assert
        onView(withId(R.id.filterClearButtonView)).check(matches(isDisplayed()))
    }

    @Test
    fun givenFilterClearButtonDisplayed_whenListItemsUnmarked_shouldDisappearClearButton() {
        // arrange
        onView(withId(R.id.filterContent)).perform(isDisplayed().waitUntil())
        onView(withText("Playstation 3")).perform(click())

        // act
        onView(withText("Playstation 3")).perform(click())

        // assert
        onView(withId(R.id.filterClearButtonView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun givenFilterWithClickedItems_whenClearFilterButtonPressed_shouldReturnToMainListing() {
        // arrange
        mockResponse200("mockjson/library/get_library_success.json".getJson())
        onView(withId(R.id.filterContent)).perform(isDisplayed().waitUntil())
        onView(withText("Playstation 3")).perform(click())
        onView(withText("Playstation 4")).perform(click())

        // act
        onView(withId(R.id.filterClearButtonView)).perform(click())

        // assert
        onView(withId(R.id.list)).check(matches(isDisplayed()))
    }

    @Test
    fun givenFilterWithClickedItems_whenFilterButtonPressed_shouldReturnToMainFilteredListing() {
        // arrange
        mockResponse200("mockjson/library/get_library_success.json".getJson())
        onView(withId(R.id.filterContent)).perform(isDisplayed().waitUntil())
        onView(withText("Playstation 3")).perform(click())
        onView(withText("Playstation 4")).perform(click())

        // act
        onView(withId(R.id.filterButtonView)).perform(click())

        // assert
        onView(withId(R.id.list)).check(matches(isDisplayed()))
    }
}