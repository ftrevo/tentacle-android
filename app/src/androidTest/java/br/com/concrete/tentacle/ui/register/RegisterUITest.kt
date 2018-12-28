package br.com.concrete.tentacle.ui.register

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseTest
import br.com.concrete.tentacle.features.register.RegisterActivity
import br.com.concrete.tentacle.ui.register.custom_actions.TentacleEditTextSetTextViewAction
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RegisterUITest : BaseTest() {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<RegisterActivity>(RegisterActivity::class.java) {}

    @Test
    fun checkEmail() {

        emptyEmail()
        invalidEmail()
        validEmail()
    }

    private fun emptyEmail() {
        Espresso.onView(withId(R.id.edtEmail)).perform(
            TentacleEditTextSetTextViewAction(
                ""
            )
        )
        callButtonClick()
        checkUIMessage()
    }

    private fun invalidEmail() {
        Espresso.onView(withId(R.id.edtEmail)).perform(
            TentacleEditTextSetTextViewAction(
                "email.invalido"
            )
        )
        callButtonClick()
        checkUIMessage()
    }

    private fun validEmail() {
        Espresso.onView(withId(R.id.edtEmail)).perform(
            TentacleEditTextSetTextViewAction(
                "email.valido@provedor.com"
            )
        )
        callButtonClick()
        Espresso.onView(withText("Digite um e-mail válido")).check(ViewAssertions.matches(not(isDisplayed())))
    }

    private fun callButtonClick() {
        Espresso.onView(withId(R.id.btnCreateAccount)).perform(click())
    }

    private fun checkUIMessage() {
        Espresso.onView(withText("Digite um e-mail válido")).check(ViewAssertions.matches(isDisplayed()))
    }
}