package br.com.concrete.tentacle.features.forgotPassword.passwordRecovery

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.testing.SingleFragmentTestActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun arrange(action: PasswordRecoveryArrange.() -> Unit) {
    PasswordRecoveryArrange().apply(action)
}

fun act(action: PasswordRecoveryAct.() -> Unit) {
    PasswordRecoveryAct().apply(action)
}

fun assert(action: PasswordRecoveryAssert.() -> Unit) {
    PasswordRecoveryAssert().apply(action)
}

class PasswordRecoveryArrange {

    fun initFragment(intentRule: IntentsTestRule<SingleFragmentTestActivity>) {
        val fragment = PasswordRecoveryFragment()
        intentRule.activity.setFragment(fragment)
    }

    fun initFragmentWithEmailParameter(intentRule: IntentsTestRule<SingleFragmentTestActivity>) {
        val fragment = PasswordRecoveryFragment.newInstance("test@test.com")
        intentRule.activity.setFragment(fragment)
    }

    fun mockErrorResponse(mockWebServer: MockWebServer) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("mockjson/errors/error_400.json".getJson())
        )
    }
}

class PasswordRecoveryAct {

    fun typeEmail() {
        typeInputField(R.id.emailEditText, "test@test.com")
    }

    fun clickRecoveryButton() {
        onView(withId(R.id.recoveryPasswordButton))
            .perform(scrollTo(), click())
    }

    fun typeToken() {
        typeInputField(R.id.tokenEditText, "AIU8D")
    }

    fun typeShortPassword() {
        typeInputField(R.id.newPassEditText, "12345")
    }

    fun typeShortPasswordConfirmation() {
        typeInputField(R.id.newPassConfirmationEditText, "12345")
    }

    fun typeCorrectPassword() {
        typeInputField(R.id.newPassEditText, "123456")
    }

    fun typeCorrectPasswordConfirmation() {
        typeInputField(R.id.newPassConfirmationEditText, "123456")
    }

    fun typeWrongPasswordConfirmation() {
        typeInputField(R.id.newPassConfirmationEditText, "1234567")
    }

    private fun typeInputField(resId: Int, text: String) {
        onView(
            withId(resId)
                .childAtPosition(0)
                .childAtPosition(1)
        ).perform(
            scrollTo(),
            replaceText(text)
        )
    }
}

class PasswordRecoveryAssert {

    fun isEmailTextEmpty() {
        isFieldEmpty(R.id.emailEditText)
    }

    fun isEmailTextWithCorrectContent() {
        onView(withId(R.id.emailEditText))
            .check(
                matches(
                    hasDescendant(withText("test@test.com"))
                )
            )
    }

    fun isTokenEmpty() {
        isFieldEmpty(R.id.tokenEditText)
    }

    fun isPasswordEmpty() {
        isFieldEmpty(R.id.newPassEditText)
    }

    fun isConfirmPasswordEmpty() {
        isFieldEmpty(R.id.newPassConfirmationEditText)
    }

    fun emailFieldErrorIsDisplayed() {
        errorById(R.id.emailEditText)?.check(
            matches(isDisplayed())
        )
    }

    fun tokenFieldErrorIsDisplayed() {
        errorById(R.id.tokenEditText)?.check(
            matches(isDisplayed())
        )
    }

    fun newPassFieldErrorIsDisplayed() {
        errorById(R.id.newPassEditText)?.check(
            matches(isDisplayed())
        )
    }

    fun confirmPassFieldErrorIsDisplayed() {
        errorById(R.id.newPassConfirmationEditText)?.check(
            matches(isDisplayed())
        )
    }

    fun shortPasswordError() {
        errorById(R.id.newPassEditText)?.check(
            matches(
                withText("O campo password precisa ter no mínimo 6 digitos")
            )
        )
    }

    fun shortConfirmationPasswordError() {
        errorById(R.id.newPassConfirmationEditText)?.check(
            matches(
                withText("O campo password precisa ter no mínimo 6 digitos")
            )
        )
    }

    fun differentPasswordsError() {
        errorById(R.id.newPassConfirmationEditText)?.check(
            matches(
                withText("Os campos senha e comfirmação de senha não coincidem")
            )
        )
    }

    fun genericResponseErrorIsDisplayed() {
        onView(withText("ERROR MESSAGE."))
            .check(matches(isDisplayed()))
    }

    private fun errorById(resId: Int): ViewInteraction? {
        return onView(
            withId(resId)
                .childAtPosition(0)
                .childAtPosition(2)
        )
    }

    private fun isFieldEmpty(@StringRes resId: Int) {
        onView(withId(resId))
            .check(
                matches(
                    hasDescendant(withText(""))
                )
            )
    }
}