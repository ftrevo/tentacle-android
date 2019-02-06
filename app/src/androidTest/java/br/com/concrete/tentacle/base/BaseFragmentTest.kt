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
import br.com.concrete.tentacle.util.LayoutChangeCallback
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
abstract class BaseFragmentTest {

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

    fun childAtPosition(
        parentMatcher: Matcher<View>,
        position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent) &&
                        view == parent.getChildAt(position)
            }
        }
    }
}