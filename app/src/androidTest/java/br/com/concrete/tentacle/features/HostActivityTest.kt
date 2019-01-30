package br.com.concrete.tentacle.features

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.testing.BottomBarTestActivity
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HostActivityTest {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<BottomBarTestActivity>(BottomBarTestActivity::class.java) {}

    @Test
    fun bottomBarNavigationHome() {
        onView(ViewMatchers.withId(R.id.bottomBar)).check(matches(isDisplayed()))

        selectedView(R.id.action_home, true)
        selectedView(R.id.action_library, false)
        selectedView(R.id.action_games, false)
        selectedView(R.id.action_reservation, false)
        selectedView(R.id.action_events, false)
    }

    @Test
    fun bottomBarNavigationLibrary() {

        onView(withId(R.id.action_library)).perform(click())

        selectedView(R.id.action_home, false)
        selectedView(R.id.action_library, true)
        selectedView(R.id.action_games, false)
        selectedView(R.id.action_reservation, false)
        selectedView(R.id.action_events, false)
    }

    @Test
    fun bottomBarNavigationGames() {

        onView(withId(R.id.action_games)).perform(click())

        selectedView(R.id.action_home, false)
        selectedView(R.id.action_library, false)
        selectedView(R.id.action_games, true)
        selectedView(R.id.action_reservation, false)
        selectedView(R.id.action_events, false)
    }

    @Test
    fun bottomBarNavigationReservation() {

        onView(withId(R.id.action_reservation)).perform(click())

        selectedView(R.id.action_home, false)
        selectedView(R.id.action_library, false)
        selectedView(R.id.action_games, false)
        selectedView(R.id.action_reservation, true)
        selectedView(R.id.action_events, false)
    }

    @Test
    fun bottomBarNavigationEvents() {

        onView(withId(R.id.action_events)).perform(click())

        selectedView(R.id.action_home, false)
        selectedView(R.id.action_library, false)
        selectedView(R.id.action_games, false)
        selectedView(R.id.action_reservation, false)
        selectedView(R.id.action_events, true)
    }

    private fun selectedView(viewId: Int, shouldBeVisible: Boolean) {
        val assertion = if (shouldBeVisible) matches(isDisplayed()) else matches(not(isDisplayed()))
        onView(allOf(withId(R.id.selectedView), isDescendantOfA(withId(viewId)))).check(assertion)
    }
}