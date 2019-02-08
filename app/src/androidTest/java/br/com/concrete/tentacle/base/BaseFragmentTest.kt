package br.com.concrete.tentacle.base

import android.Manifest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import br.com.concrete.tentacle.testing.SingleFragmentTestActivity
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
}