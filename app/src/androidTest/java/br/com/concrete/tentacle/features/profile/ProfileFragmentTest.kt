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
import org.junit.Before
import org.junit.Test

class ProfileFragmentTest: BaseFragmentTest() {

    /**
     * Set this variable to true to see Loading error screen
     */
    private var SHOW_LOADING_ERROR_SCREEN: Boolean

    init {
        SHOW_LOADING_ERROR_SCREEN = true
    }

    override fun setupFragment() {
        testFragment = ProfileFragment()
    }

    @Before
    fun setMock() {
        if (!SHOW_LOADING_ERROR_SCREEN) {
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
        } else {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(400)
                    .setBody("mockjson/profile/get_profile_error.json".getJson())
            )
        }
    }

    @Test
    fun checkAutofillFields() {
        if (!SHOW_LOADING_ERROR_SCREEN) {
            checkInputField("Damiana dos toró", R.id.userNameEditText)
            checkInputField("dragons@best.com", R.id.emailEditText)
            checkInputField("(81) 95874-2360", R.id.phoneEditText)

            checkStateSelection("PE")
            checkCitySelection("Recife")
        }
    }

    @Test
    fun checkLoadErrorScreen() {
        if (SHOW_LOADING_ERROR_SCREEN) {
            onView(withId(R.id.loadError))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun checkReloadContentOnErrorScreen() {
        if (SHOW_LOADING_ERROR_SCREEN) {
            onView(withId(R.id.loadError))
                .check(matches(isDisplayed()))

            SHOW_LOADING_ERROR_SCREEN = false
            setMock()
            clickButton(R.id.buttonNameError)

            checkAutofillFields()
        }
    }

    @Test
    fun checkEmptyName() {
        if (!SHOW_LOADING_ERROR_SCREEN) {
            setField("", R.id.userNameEditText)
            clickButton(R.id.saveChangesButton)
            matchesIsDisplayed(R.string.name_error)
        }
    }

    @Test
    fun checkValidName() {
        if (!SHOW_LOADING_ERROR_SCREEN) {
            setField("Teste Teste", R.id.userNameEditText)

            setField("", R.id.emailEditText)    //force this error in order to only make validations after click
            clickButton(R.id.saveChangesButton)
            matchesNotIsDisplayed(R.string.name_error)
        }
    }

    @Test
    fun checkInvalidEmail() {
        if (!SHOW_LOADING_ERROR_SCREEN) {
            setField("teste@teste", R.id.emailEditText)
            clickButton(R.id.saveChangesButton)
            matchesIsDisplayed(R.string.email_error)
        }
    }
    @Test
    fun checkValidEmail() {
        if (!SHOW_LOADING_ERROR_SCREEN) {

            setField("", R.id.userNameEditText)    //force this error in order to only make validations after click
            setField("teste@teste.com", R.id.emailEditText)
            clickButton(R.id.saveChangesButton)
            matchesNotIsDisplayed(R.string.email_error)
        }
    }

    @Test
    fun checkEmptyPhone() {
        if (!SHOW_LOADING_ERROR_SCREEN) {
            setField("", R.id.phoneEditText)
            clickButton(R.id.saveChangesButton)
            matchesIsDisplayed(R.string.phone_error)
        }
    }
    @Test
    fun checkInvalidPhone() {
        if (!SHOW_LOADING_ERROR_SCREEN) {
            setField("999999999", R.id.phoneEditText)
            clickButton(R.id.saveChangesButton)
            matchesIsDisplayed(R.string.phone_error)
        }
    }
    @Test
    fun checkValidPhone() {
        if (!SHOW_LOADING_ERROR_SCREEN) {
            setField("99 999999999", R.id.phoneEditText)

            setField("", R.id.emailEditText)    //force this error in order to only make validations after click
            clickButton(R.id.saveChangesButton)
            matchesNotIsDisplayed(R.string.phone_error)
        }
    }

    private fun clickButton(buttonResId: Int) {
        onView(withId(buttonResId))
            .perform(scrollTo(),click())
    }

    private fun matchesNotIsDisplayed(idMessageError: Int) {
        onView(withText(idMessageError))
            .check(matches(not(isDisplayed()))
        )
    }

    private fun matchesIsDisplayed(idMessageError: Int) {
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