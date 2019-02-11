package br.com.concrete.tentacle.features

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
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
class HostActivityLogoutTest {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<HostActivity>(HostActivity::class.java) {}

    @Test
    fun logout(){
        onView(withId(R.id.logout)).perform(click())

        //TODO Figure out how to check dialog title. This approach does not work
//        val titleId = activityTestRule.activity.resources.getIdentifier("alertTitle", "id", "android")
//        onView(withId(titleId))
//            .inRoot(isDialog())
//            .check(matches(withText("Logout")))
//            .check(matches(isDisplayed()))


        onView(withId(android.R.id.message))
            .inRoot(isDialog())
            .check(matches(withText("Tem certeza que deseja efetuar o logout?")))
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.button2))
            .inRoot(isDialog())
            .check(matches(withText("CANCELAR")))
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.button1))
            .inRoot(isDialog())
            .check(matches(withText("OK")))
            .check(matches(isDisplayed()))

    }
}