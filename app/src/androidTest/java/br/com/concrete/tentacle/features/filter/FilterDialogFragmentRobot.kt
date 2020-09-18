package br.com.concrete.tentacle.features.filter

import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.filter.SubItem
import br.com.concrete.tentacle.extensions.waitUntil
import org.hamcrest.Matchers.not

val fragment = MyFragment()

fun arrange(action: FilterArrange.() -> Unit) {
    FilterArrange().apply(action)
}

fun act(action: FilterAct.() -> Unit) {
    FilterAct().apply(action)
}

fun assert(action: FilterAssert.() -> Unit) {
    FilterAssert().apply(action)
}

class FilterArrange {

    fun initFragment() {
        FilterDialogFragment.showDialog(fragment, ArrayList(), "dummy_filter_itens_library.json")
    }

}

class FilterAct {

    fun clickPS3() {
        onView(withText("Playstation 3")).perform(click())
    }

    fun clickPS4() {
        onView(withText("Playstation 4")).perform(click())
    }

    fun clickFilterButton() {
        onView(withId(R.id.filterButtonView)).perform(click())
    }

    fun clickCloseButton() {
        onView(withId(R.id.filterCloseButton)).perform(click())
    }

    fun clickClearButton() {
        onView(withId(R.id.filterClearButtonView)).perform(click())
    }

    fun waitFilterDisplay() {
        onView(withId(R.id.filterContent)).perform(isDisplayed().waitUntil())
    }

}

class FilterAssert {

    fun clearButtonIsDisplayed() {
        onView(withId(R.id.filterClearButtonView))
            .check(matches(isDisplayed()))
    }

    fun clearButtonIsNotDisplayed() {
        onView(withId(R.id.filterClearButtonView))
            .check(matches(not(isDisplayed())))
    }

    fun isDialogDismissed() {
        onView(withText("Tentacle"))
            .check(
                matches(
                    isDisplayed()
                )
            )
    }

}

class MyFragment : Fragment(), FilterDialogFragment.OnFilterListener {

    override fun onFilterListener(filters: List<SubItem>) {}

}