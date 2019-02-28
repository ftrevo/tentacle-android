package br.com.concrete.tentacle.matchers

import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class IsTextInLines(private val lines: Int) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("IsTextInLines")
    }

    override fun matchesSafely(item: View?): Boolean {
        return (item as TextView).lineCount == lines
    }
}