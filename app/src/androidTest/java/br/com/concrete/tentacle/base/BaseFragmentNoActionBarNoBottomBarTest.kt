package br.com.concrete.tentacle.base

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.testing.SingleFragmentTestActivityNoActionBarNoBottomBar
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule

@Ignore
abstract class BaseFragmentNoActionBarNoBottomBarTest: BaseInstrumentedTest() {

    @get:Rule
    val activityRule = ActivityTestRule(SingleFragmentTestActivityNoActionBarNoBottomBar::class.java)

    abstract fun setupFragment()

    @Before
    open fun before() {
        setupFragment()

        testFragment.let {
            activityRule.activity.setFragment(it)
        }
    }

    fun setField(textField: String, id: Int) {
        onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edt),
                ViewMatchers.withId(id)
                    .childAtPosition(0)
                    .childAtPosition(1)
            )
        ).perform(
            ViewActions.scrollTo(),
            ViewActions.replaceText(textField)
        )
    }
}