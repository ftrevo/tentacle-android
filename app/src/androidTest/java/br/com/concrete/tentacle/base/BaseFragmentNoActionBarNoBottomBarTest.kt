package br.com.concrete.tentacle.base

import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.testing.SingleFragmentTestActivityNoActionBarNoBottomBar
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
}