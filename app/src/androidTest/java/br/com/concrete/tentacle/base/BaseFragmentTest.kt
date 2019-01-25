package br.com.concrete.tentacle.base

import androidx.fragment.app.Fragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.testing.SingleFragmentTestActivity
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(SingleFragmentTestActivity::class.java)

    lateinit var mockWebServer: MockWebServer
    lateinit var testFragment: Fragment

    abstract fun setupFragment()

    @Before
    open fun before() {
        setupFragment()

        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        testFragment.let {
            activityRule.activity.setFragment(it)
        }
    }

    @After
    fun after() {
        mockWebServer.shutdown()
    }

}