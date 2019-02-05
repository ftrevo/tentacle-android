package br.com.concrete.tentacle.base

import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.testing.SingleFragmentTestActivity
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule

@Ignore
abstract class BaseFragmentTest: BaseInstrumentedTest() {

    @get:Rule
    val activityRule = ActivityTestRule(SingleFragmentTestActivity::class.java)

    abstract fun setupFragment()

    @Before
    open fun before() {
        setupFragment()

        testFragment.let {
            activityRule.activity.setFragment(it)
        }
    }
}