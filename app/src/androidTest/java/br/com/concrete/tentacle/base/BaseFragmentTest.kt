package br.com.concrete.tentacle.base

import android.Manifest
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.testing.SingleFragmentTestActivity
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.rules.RuleChain

@Ignore
abstract class BaseFragmentTest : BaseInstrumentedTest() {

    @get:Rule
    val activityRule = ActivityTestRule(SingleFragmentTestActivity::class.java)

    @get:Rule
    val screenshotTestRule = ScreenshotTestRule()

    @get:Rule
    val screenShotRule = RuleChain
        .outerRule(GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        .around(ScreenshotTestRule())

    abstract fun setupFragment()

    @Before
    open fun before() {
        setupFragment()

        testFragment.let {
            activityRule.activity.setFragment(it)
        }
    }

    fun setField(textField: String, id: Int) {
        Espresso.onView(
            Matchers.allOf(
                withId(R.id.edt),
                withId(id)
                    .childAtPosition(0)
                    .childAtPosition(1)
            )
        ).perform(
            scrollTo(),
            replaceText(textField)
        )
    }

    fun checkInputField(textField: String, id: Int) {
        Espresso.onView(
            Matchers.allOf(
                withId(R.id.edt),
                withId(id)
                    .childAtPosition(0)
                    .childAtPosition(1)
            )
        ).check(matches(withText(textField)))
    }
}