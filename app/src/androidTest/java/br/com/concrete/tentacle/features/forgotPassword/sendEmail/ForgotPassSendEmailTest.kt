package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.childAtPosition
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForgotPassSendEmailTest {

    val mockWebServer = MockWebServer()

    @get:Rule
    var activityTestRule = IntentsTestRule(ForgotPassSendEmailActivity::class.java)

    @Before
    fun startServer() {
        mockWebServer.start(8080)
    }

    @After
    fun shutdownServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun checkValidEmailInvalid() {
        act {}
        assert {}
        onView(
            withId(R.id.edtEmail)
                .childAtPosition(0)
                .childAtPosition(1)
        ).perform(
            scrollTo(),
            replaceText("teste@teste")
        )

        clickButton()
        onView(withText("Digite um e-mail válido")).check(matches(isDisplayed()))
    }

    @Test
    fun checkValidEmailInvalidEmpty() {
        act {}
        assert {}
        onView(
            withId(R.id.edtEmail)
                .childAtPosition(0)
                .childAtPosition(1)
        ).perform(
            scrollTo(),
            replaceText("")
        )

        clickButton()
        onView(withText("Digite um e-mail válido")).check(matches(isDisplayed()))
    }

    @Test
    fun sendEmailForgotPassword() {
        arrange {
            mockSuccess(mockWebServer)
        }
        act {}
        assert {}
        onView(
            withId(R.id.edtEmail)
                .childAtPosition(0)
                .childAtPosition(1)
        ).perform(
            scrollTo(),
            replaceText("teste@teste.com")
        )

        clickButton()
    }

    @Test
    fun sendEmailForgotPasswordError400() {
        arrange {
            mockError(mockWebServer)
        }
        act {}
        assert {}
        checkFields()
    }

    @Test
    fun sendEmailForgotPasswordError404() {
        arrange {
            mockError(mockWebServer)
        }
        act {}
        assert {}
        checkFields()
    }

    private fun checkFields() {
        onView(
            withId(R.id.edtEmail)
                .childAtPosition(0)
                .childAtPosition(1)
        ).perform(
            scrollTo(),
            replaceText("teste@teste.com")
        )

        clickButton()
        onView(withText("ERROR MESSAGE.")).check(matches(isDisplayed()))
    }

    private fun clickButton() {
        onView(withId(R.id.btSend))
            .check(matches(isDisplayed()))
            .perform(click())
    }
}