package br.com.concrete.tentacle.base

import android.app.Fragment
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.testing.SingleFragmentTestActivity
import br.com.concrete.tentacle.util.LayoutChangeCallback
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.runner.RunWith

@Ignore
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

    fun getJson(path: String): String {
        val json = getInstrumentation()
            .targetContext
            .assets
            .open(path).bufferedReader().use { it.readText() }

        return json
    }

    fun waitUntil(matcher: Matcher<View>): ViewAction {
        return ViewActions.actionWithAssertions(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(View::class.java)
            }

            override fun getDescription(): String {
                val description = StringDescription()
                matcher.describeTo(description)
                return String.format("wait until: %s", description)
            }

            override fun perform(uiController: UiController, view: View) {
                if (!matcher.matches(view)) {
                    val callback = LayoutChangeCallback(matcher)
                    try {
                        IdlingRegistry.getInstance().register(callback)
                        view.addOnLayoutChangeListener(callback)
                        uiController.loopMainThreadUntilIdle()
                    } finally {
                        view.removeOnLayoutChangeListener(callback)
                        IdlingRegistry.getInstance().unregister(callback)
                    }
                }
            }
        })
    }
}