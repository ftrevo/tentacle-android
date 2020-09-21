package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.features.forgotPassword.passwordRecovery.PasswordRecoveryActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun arrange(action: ForgotPassSendEmailArrange.() -> Unit) {
    ForgotPassSendEmailArrange().apply(action)
}

fun act(action: ForgotPassSendEmailAct.() -> Unit) {
    ForgotPassSendEmailAct().apply(action)
}

fun assert(action: ForgotPassSendEmailAssert.() -> Unit) {
    ForgotPassSendEmailAssert().apply(action)
}

class ForgotPassSendEmailArrange {

    fun mockSuccessResponse(mockWebServer: MockWebServer) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/forgotPassword/sendEmail/user.json".getJson())
        )
    }

    fun mockErrorResponse(mockWebServer: MockWebServer) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("mockjson/errors/error_400.json".getJson())
        )
    }

}

class ForgotPassSendEmailAct {

    fun clickSendButton() {
        clickButton(R.id.btSend)
    }

    fun clickTokenText() {
        clickButton(R.id.tvAlreadyHaveToken)
    }

    fun typeInvalidEmail() {
        typeInputField(R.id.edtEmail, "teste@teste")
    }

    fun typeValidEmail() {
        typeInputField(R.id.edtEmail, "teste@teste.com")
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

    private fun clickButton(id: Int) {
        onView(withId(id))
            .perform(scrollTo(), click())
    }
}

class ForgotPassSendEmailAssert {

    fun isInvalidEmailErrorDisplayed() {
        errorById(R.id.edtEmail)?.check(
            matches(withText("Digite um e-mail válido"))
        )?.check(matches(isDisplayed()))
    }

    fun checkGoToPassRecovery() {
        Intents.intended(IntentMatchers.hasComponent(PasswordRecoveryActivity::class.java.name))
    }

    fun showGenericError() {
        onView(withText("ERROR MESSAGE."))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    private fun errorById(resId: Int): ViewInteraction? {
        return onView(
            withId(resId)
                .childAtPosition(0)
                .childAtPosition(2)
        )
    }

}