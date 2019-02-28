package br.com.concrete.tentacle.actions

import android.view.View
import android.widget.TextView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher

class SetTextInTextView(private val text: String) : ViewAction {
    override fun getDescription(): String {
        return "Set text in TextView"
    }

    override fun getConstraints(): Matcher<View> {
        return  isAssignableFrom(TextView::class.java)
    }

    override fun perform(uiController: UiController?, view: View?) {
        (view as TextView).text = text
    }
}