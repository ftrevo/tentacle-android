package br.com.concrete.tentacle.ui.register

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseTest
import br.com.concrete.tentacle.features.register.RegisterActivity
import br.com.concrete.tentacle.ui.register.custom_actions.TentacleEditTextSetTextViewAction
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RegisterUITest : BaseTest() {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<RegisterActivity>(RegisterActivity::class.java) {}

    private val messageErrorEmail =  "Digite um e-mail válido"
    private val messageErrorName =  "O campo nome é de preenchimento obrigatório"
    private val messageErrorPassword =  "O campo password precisa ter no mínimo 6 digitos"
    private val messageErrorPhone =  "Forneca um número de telefone válido"
    private val messageErrorState =  "Selecione um estado"


    //EMAIL
    @Test
    fun checkEmptyEmail() {
        setField("", R.id.edtEmail)
        matchesIsDesplayed(messageErrorEmail)
    }
    @Test
    fun checkInvalidEmail() {
        setField("teste@teste", R.id.edtEmail)
        matchesIsDesplayed(messageErrorEmail)
    }
    @Test
    fun checkValidEmail() {
        setField("teste@teste.com", R.id.edtEmail)
        matchesNotIsDesplayed(messageErrorEmail)
    }


    //NAME
    @Test
    fun checkEmptyName() {
        presetValidForm()
        setField("", R.id.edtUserName)
        matchesIsDesplayed(messageErrorName)
    }
    @Test
    fun checkInvalidName() {
        presetValidForm()
        setField("", R.id.edtUserName)
        matchesIsDesplayed(messageErrorName)
    }
    @Test
    fun checkValidName() {
        presetValidForm()
        setField("Teste Teste", R.id.edtUserName)
        matchesNotIsDesplayed(messageErrorName)
    }


    //PASSWORD
    @Test
    fun checkEmptyPassword() {
        presetValidForm()
        setField("", R.id.edtPassword)
        matchesIsDesplayed(messageErrorPassword)
    }
    @Test
    fun checkInvalidPassword() {
        presetValidForm()
        setField("123", R.id.edtPassword)
        matchesIsDesplayed(messageErrorPassword)
    }
    @Test
    fun checkValidPassword() {
        presetValidForm()
        setField("123456", R.id.edtPassword)
        matchesNotIsDesplayed(messageErrorPassword)
    }


    //PHONE
    @Test
    fun checkEmptyPhone() {
        presetValidForm()
        setField("", R.id.edtPhone)
        matchesIsDesplayed(messageErrorPhone)
    }
    @Test
    fun checkInvalidPhone() {
        presetValidForm()
        setField("999999999", R.id.edtPhone)
        matchesIsDesplayed(messageErrorPhone)
    }
    @Test
    fun checkValidPhone() {
        presetValidForm()
        setField("99 999999999", R.id.edtPhone)
        matchesNotIsDesplayed(messageErrorPhone)
    }


    //State Spinner
    @Test
    fun checkEmptyStateSelection() {
        presetValidForm()
        matchesIsDesplayed(messageErrorState)
    }

    @Test
    fun checkStateSelection() {
        presetValidForm()
        matchesIsDesplayed(messageErrorState)
    }

    private fun setField(textField: String, id: Int) {
        Espresso.onView(withId(id)).perform(TentacleEditTextSetTextViewAction(textField))

        callButtonClick()
    }

    @Test
    private fun callButtonStateClick() {
        Espresso.onView(withId(R.id.spState)).perform(click())
    }

    private fun callButtonClick() {
        Espresso.onView(withId(R.id.btnCreateAccount)).perform(click())
    }

    private fun matchesNotIsDesplayed(messageError: String) {
        Espresso.onView(withText(messageError)).check(ViewAssertions.matches(not(isDisplayed())))
    }

    private fun matchesIsDesplayed(messageError: String) {
        Espresso.onView(withText(messageError)).check(ViewAssertions.matches(isDisplayed()))
    }

    private fun presetValidForm() {
        setField("teste@teste.com", R.id.edtEmail)
        setField("Teste Teste", R.id.edtUserName)
        setField("123456", R.id.edtPassword)
        setField("99 999999999", R.id.edtPhone)
    }

}