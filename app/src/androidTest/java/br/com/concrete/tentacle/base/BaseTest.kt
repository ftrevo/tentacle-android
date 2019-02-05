package br.com.concrete.tentacle.base

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.concrete.tentacle.util.LayoutChangeCallback
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
abstract class BaseTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var testFragment: Fragment

    @Before
    fun setUp(){
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    fun after() {
        mockWebServer.shutdown()
    }

    fun getJson(path: String): String {
        val json = InstrumentationRegistry.getInstrumentation()
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