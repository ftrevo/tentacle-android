package br.com.concrete.tentacle.features.forgotPassword.passwordRecovery

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.extensions.getJson
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.Matchers.not
import org.junit.Test

class PasswordRecoveryFragmentTest : BaseFragmentTest() {

    override fun setupFragment() {
        val emailTest = "email@email.com"
        testFragment = PasswordRecoveryFragment.newInstance(emailTest)
    }

    @Test
    fun shouldFillEmailInputFieldWhenHasIntentExtras() {
        onView(withId(R.id.emailEditText))
            .check(matches(hasDescendant(withText("email@email.com"))))
    }

    @Test
    fun shouldShowInputFieldsErrorsWhenNothingIsFilled() {
        setInputField(R.id.emailEditText, "")
        performClickButton()

        checkErrorById(R.id.emailEditText) {
            matches(isDisplayed())
        }

        checkErrorById(R.id.tokenEditText) {
            matches(isDisplayed())
        }

        checkErrorById(R.id.newPassEditText) {
            matches(isDisplayed())
        }

        checkErrorById(R.id.newPassConfirmationEditText) {
            matches(not(isDisplayed()))
        }
    }

    @Test
    fun invalidPassword() {
        setInputField(R.id.tokenEditText, "AIU8D")
        setInputField(R.id.newPassEditText, "12345")
        performClickButton()

        checkErrorById(R.id.newPassEditText) {
            matches(isDisplayed())
        }
        checkErrorById(R.id.newPassEditText) {
            matches(withText("O campo password precisa ter no mínimo 6 digitos"))
        }
    }

    @Test
    fun invalidConfirmationPassword() {
        setInputField(R.id.tokenEditText, "AIU8D")
        setInputField(R.id.newPassEditText, "123456")
        setInputField(R.id.newPassConfirmationEditText, "12345")
        performClickButton()

        checkErrorById(R.id.newPassConfirmationEditText) {
            matches(isDisplayed())
        }
        checkErrorById(R.id.newPassConfirmationEditText) {
            matches(withText("O campo password precisa ter no mínimo 6 digitos"))
        }
    }

    @Test
    fun passwordsDoesNotMatch() {
        setInputField(R.id.tokenEditText, "AIU8D")
        setInputField(R.id.newPassEditText, "123456")
        setInputField(R.id.newPassConfirmationEditText, "123457")
        performClickButton()

        checkErrorById(R.id.newPassConfirmationEditText) {
            matches(isDisplayed())
        }
        checkErrorById(R.id.newPassConfirmationEditText) {
            matches(withText("Os campos senha e comfirmação de senha não coincidem"))
        }
    }

    @Test
    fun restorePasswordFailInvalidToken() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("mockjson/errors/error_400.json".getJson())
        )

        setInputField(R.id.tokenEditText, "AIU8D")
        setInputField(R.id.newPassEditText, "123456")
        setInputField(R.id.newPassConfirmationEditText, "123456")
        performClickButton()
        onView(withText("ERROR MESSAGE.")).check(matches(isDisplayed()))
    }

    private fun setInputField(resId: Int, text: String) {
        onView(withId(resId)
                .childAtPosition(0)
                .childAtPosition(1)
        ).perform(
            scrollTo(),
            replaceText(text)
        )
    }

    private fun checkErrorById(resId: Int, func: () -> ViewAssertion) {
        onView(withId(resId)
            .childAtPosition(0)
            .childAtPosition(2))
            .check(
                func()
            )
    }

    private fun performClickButton() {
        onView(withId(R.id.recoveryPasswordButton))
            .perform(scrollTo(), click())
    }
}