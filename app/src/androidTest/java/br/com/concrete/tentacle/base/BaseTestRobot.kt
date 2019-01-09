package br.com.concrete.tentacle.base

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.ui.custom.TentacleEditTextSetTextViewAction
import org.hamcrest.Matchers.not


open class BaseTestRobot {

    fun fillEditText(resourceId: Int, text: String): ViewInteraction =
        onView(withId(resourceId)).perform(TentacleEditTextSetTextViewAction(text))

    fun matchTextError(message: String): ViewInteraction =
        onView(withText(message)).check(matches(isDisplayed()))

    fun matchDisplayed(id: Int): ViewInteraction =
        onView(withId(id)).check(matches(isDisplayed()))

    fun matchWithoutTextError(message: String): ViewInteraction =
        onView(withText(message)).check(matches(not(isDisplayed())))

    fun matchWithoutSnackBar(message: String): ViewInteraction =
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(not(isDisplayed())))

    fun actionClick(resourceId: Int): ViewInteraction =
        onView(withId(resourceId)).perform(ViewActions.click())

    inline fun <reified T : Activity?> stringResource(res: Int, activityTestRule: ActivityTestRule<T>): String =
        if (res != -1)activityTestRule.activity!!.getString(res) else ""

}