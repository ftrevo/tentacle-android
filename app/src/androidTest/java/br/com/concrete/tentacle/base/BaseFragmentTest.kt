package br.com.concrete.tentacle.base

import android.Manifest
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import br.com.concrete.tentacle.testing.SingleFragmentTestActivity
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@Ignore
abstract class BaseFragmentTest: BaseInstrumentedTest() {

    @get:Rule
    val activityRule = ActivityTestRule(SingleFragmentTestActivity::class.java)

    @get:Rule
    val screenshotTestRule = ScreenshotTestRule()

    @get:Rule
    val screenShotRule = RuleChain
        .outerRule(GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        .around(ScreenshotTestRule())

    lateinit var mockWebServer: MockWebServer
    lateinit var testFragment: Fragment

    abstract fun setupFragment()

    @Before
    open fun before() {
        setupFragment()

        testFragment.let {
            activityRule.activity.setFragment(it)
        }
    }
}