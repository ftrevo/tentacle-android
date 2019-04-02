package br.com.concrete.tentacle.features.profile

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.getJson
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class ProfileFragmentTest : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = ProfileFragment()
    }

    @Test
    fun checkAutofillFields() {
        mockResult200()
        checkInputField("Damiana dos toró", R.id.userNameEditText)
        checkInputField("dragons@best.com", R.id.emailEditText)
        checkInputField("(81) 95874-2360", R.id.phoneEditText)
        checkStateSelection("PE")
        checkCitySelection("Recife")
    }

    @Test
    fun checkLoadErrorScreen() {
        mockResult400()
        onView(withId(R.id.loadError))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkReloadContentOnErrorScreen() {
        mockResult400()
        mockResult200()
        onView(withId(R.id.loadError))
            .check(matches(isDisplayed()))
        clickButton(R.id.buttonNameError)
        checkAutofillFields()
    }

    @Test
    fun checkEmptyName() {
        mockResult200()
        setField("", R.id.userNameEditText)
        clickButton(R.id.saveChangesButton)
        matchesIsDisplayed("O campo nome é de preenchimento obrigatório")
    }

    @Test
    fun checkValidName() {
        mockResult200()
        setField("Teste Teste", R.id.userNameEditText)
        setField("", R.id.emailEditText) // force this error in order to only make validations after click
        clickButton(R.id.saveChangesButton)
        matchesNotIsDisplayed("O campo nome é de preenchimento obrigatório")
    }

    @Test
    fun checkInvalidEmail() {
        mockResult200()
        setField("teste@teste", R.id.emailEditText)
        clickButton(R.id.saveChangesButton)
        matchesIsDisplayed("Digite um e-mail válido")
    }

    @Test
    fun checkValidEmail() {
        mockResult200()
        setField("", R.id.userNameEditText) // force this error in order to only make validations after click
        setField("teste@teste.com", R.id.emailEditText)
        clickButton(R.id.saveChangesButton)
        matchesNotIsDisplayed("Digite um e-mail válido")
    }

    @Test
    fun checkEmptyPhone() {
        mockResult200()
        setField("", R.id.phoneEditText)
        clickButton(R.id.saveChangesButton)
        matchesIsDisplayed("Forneça um número de telefone válido")
    }

    @Test
    fun checkInvalidPhone() {
        mockResult200()
        setField("999999999", R.id.phoneEditText)
        clickButton(R.id.saveChangesButton)
        matchesIsDisplayed("Forneça um número de telefone válido")
    }

    @Test
    fun checkValidPhone() {
        mockResult200()
        setField("99 999999999", R.id.phoneEditText)
        setField("", R.id.emailEditText) // force this error in order to only make validations after click
        clickButton(R.id.saveChangesButton)
        matchesNotIsDisplayed("Forneça um número de telefone válido")
    }

    private fun mockResult200() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/profile/get_profile_success.json".getJson())
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/profile/get_states_success.json".getJson())
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/profile/get_cities_success.json".getJson())
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/profile/change_profile_success.json")
        )
    }

    private fun mockResult400() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("mockjson/profile/get_profile_error.json".getJson())
        )
    }

    private fun clickButton(buttonResId: Int) {
        onView(withId(buttonResId))
            .perform(scrollTo(), click())
    }

    private fun matchesNotIsDisplayed(idMessageError: String) {
        onView(withText(idMessageError))
            .check(
                matches(not(isDisplayed()))
            )
    }

    private fun matchesIsDisplayed(idMessageError: String) {
        onView(withText(idMessageError))
            .check(matches(isDisplayed()))
    }

    private fun checkStateSelection(state: String) {
        onView(withId(R.id.stateSearchSpinner))
            .check(matches(hasDescendant(withText(state))))
    }

    private fun checkCitySelection(city: String) {
        onView(withId(R.id.citySearchSpinner))
            .check(matches(hasDescendant(withText(city))))
    }
}