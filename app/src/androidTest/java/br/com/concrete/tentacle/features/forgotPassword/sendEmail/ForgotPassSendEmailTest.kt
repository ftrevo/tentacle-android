package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseInstrumentedTest
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.extensions.getJson
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForgotPassSendEmailTest : BaseInstrumentedTest() {

    @get:Rule
    var activityTestRule =
        object : ActivityTestRule<ForgotPassSendEmailActivity>(ForgotPassSendEmailActivity::class.java) {}

    @Test
    fun checkValidEmailInvalid() {
        onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edt),
                ViewMatchers.withId(R.id.edtEmail)
                    .childAtPosition(0)
                    .childAtPosition(1)
            )
        ).perform(
            ViewActions.scrollTo(),
            ViewActions.replaceText("teste@teste")
        )

        clickButton()
        onView(withText("Digite um e-mail válido")).check(matches(isDisplayed()))
    }

    @Test
    fun checkValidEmailInvalidEmpty() {
        onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edt),
                ViewMatchers.withId(R.id.edtEmail)
                    .childAtPosition(0)
                    .childAtPosition(1)
            )
        ).perform(
            ViewActions.scrollTo(),
            ViewActions.replaceText("")
        )

        clickButton()
        onView(withText("Digite um e-mail válido")).check(matches(isDisplayed()))
    }

    @Test
    fun sendEmailForgotPassword() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/forgotPassword/sendEmail/user.json".getJson())
        )

        onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edt),
                ViewMatchers.withId(R.id.edtEmail)
                    .childAtPosition(0)
                    .childAtPosition(1)
            )
        ).perform(
            ViewActions.scrollTo(),
            ViewActions.replaceText("teste@teste.com")
        )

        clickButton()
    }

    @Test
    fun sendEmailForgotPasswordError400() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("mockjson/errors/error_400.json".getJson())
        )
        checkFields()
    }

    @Test
    fun sendEmailForgotPasswordError404() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("mockjson/errors/error_404.json".getJson())
        )
        checkFields()
    }

    private fun checkFields() {
        onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edt),
                ViewMatchers.withId(R.id.edtEmail)
                    .childAtPosition(0)
                    .childAtPosition(1)
            )
        ).perform(
            ViewActions.scrollTo(),
            ViewActions.replaceText("teste@teste.com")
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