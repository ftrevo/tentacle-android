package br.com.concrete.tentacle.util

import android.view.View
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.StringDescription

fun Matcher<View>.waitUntil(): ViewAction {
    val matcher = this
    return ViewActions.actionWithAssertions(object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(View::class.java)
        }

        override fun getDescription(): String {
            val description = StringDescription()
            matcher.describeTo(description)
            return String.format("wait until: %s", description)
        }

        override fun perform(uiController: UiController, view: View) {
            if (!matcher.matches(view)) {
                val callback = LayoutChangeCallback(matcher)
                try {
                    IdlingRegistry.getInstance().register(callback)
                    view.addOnLayoutChangeListener(callback)
                    uiController.loopMainThreadUntilIdle()
                } finally {
                    view.removeOnLayoutChangeListener(callback)
                    IdlingRegistry.getInstance().unregister(callback)
                }
            }
        }
    })
}