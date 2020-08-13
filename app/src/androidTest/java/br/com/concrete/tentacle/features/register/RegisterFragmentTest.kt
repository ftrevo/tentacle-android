package br.com.concrete.tentacle.features.register

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentNoActionBarNoBottomBarTest
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.waitUntil
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterFragmentTest : BaseFragmentNoActionBarNoBottomBarTest() {

    override fun setupFragment() {
        testFragment = RegisterFragment()
    }

    @Before
    fun setMock() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/register/get_states_success.json".getJson())
        )
    }

    @Test
    fun checkInvalidEmail() {
        presetValidForm()
        setField("teste@teste", R.id.edtEmail)
        callButtonClick()
        matchesIsDisplayed(R.string.email_error)
    }
    @Test
    fun checkValidEmail() {
        presetValidForm()
        setField("teste@teste.com", R.id.edtEmail)
        callButtonClick()
        matchesNotIsDisplayed(R.string.email_error)
    }

    @Test
    fun checkEmptyName() {
        presetValidForm()
        setField("", R.id.edtUserName)
        callButtonClick()
        matchesIsDisplayed(R.string.name_error)
    }
    @Test
    fun checkInvalidName() {
        presetValidForm()
        setField("", R.id.edtUserName)
        callButtonClick()
        matchesIsDisplayed(R.string.name_error)
    }
    @Test
    fun checkValidName() {
        presetValidForm()
        setField("Teste Teste", R.id.edtUserName)
        callButtonClick()
        matchesNotIsDisplayed(R.string.name_error)
    }

    @Test
    fun checkEmptyPassword() {
        presetValidForm()
        setField("", R.id.edtPassword)
        callButtonClick()
        matchesIsDisplayed(R.string.password_error)
    }
    @Test
    fun checkInvalidPassword() {
        presetValidForm()
        setField("123", R.id.edtPassword)
        callButtonClick()
        matchesIsDisplayed(R.string.password_error)
    }
    @Test
    fun checkValidPassword() {
        presetValidForm()
        setField("123456", R.id.edtPassword)
        callButtonClick()
        matchesNotIsDisplayed(R.string.password_error)
    }

    @Test
    fun checkEmptyPhone() {
        presetValidForm()
        setField("", R.id.edtPhone)
        callButtonClick()
        matchesIsDisplayed(R.string.phone_error)
    }
    @Test
    fun checkInvalidPhone() {
        presetValidForm()
        setField("999999999", R.id.edtPhone)
        callButtonClick()
        matchesIsDisplayed(R.string.phone_error)
    }
    @Test
    fun checkValidPhone() {
        presetValidForm()
        setField("99 999999999", R.id.edtPhone)
        callButtonClick()
        matchesNotIsDisplayed(R.string.phone_error)
    }

    @Test
    fun checkEmptyStateSelection() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/register/get_cities_success.json".getJson())
        )

        presetValidForm()
        callButtonClick()
        matchesIsDisplayed(R.string.state_error)
    }

    @Test
    fun checkValidStateSelection() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/register/get_cities_success.json".getJson())
        )

        presetValidForm()
        selectState()
        callButtonClick()
        matchesNotIsDisplayed(R.string.state_error)
    }

    @Test
    fun checkEmptyCitySelection() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/register/get_cities_success.json".getJson())
        )

        presetValidForm()
        selectState()
        callButtonClick()
        matchesIsDisplayed(R.string.city_error)
    }

    @Test
    fun registerSuccess() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/register/get_cities_success.json".getJson())
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/login/login_success.json".getJson())
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/token/token_update_success.json".getJson())
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/home/load_home_games_success.json".getJson())
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/profile/get_profile_success.json".getJson()))

        presetValidForm()
        selectState()
        selectCity()
        callButtonClick()
        checkIfGoesToHome()
    }

    @Test
    fun registerError400() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/register/get_cities_success.json".getJson())
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("mockjson/errors/error_400.json".getJson())
        )

        presetValidForm()
        selectState()
        selectCity()
        callButtonClick()
        onView(withText("ERROR MESSAGE.")).check(matches(isDisplayed()))
    }

    private fun selectState() {
        onView(
            Matchers.allOf(
                withId(R.id.button), withText("Selecione"),
                withId(R.id.spState)
                    .childAtPosition(0)
                    .childAtPosition(1)
            )
        ).perform(ViewActions.scrollTo(), click())

        onData(Matchers.anything())
            .inAdapterView(
                Matchers.allOf(
                    withId(R.id.list),
                    withClassName(
                        Matchers.`is`("android.widget.LinearLayout"))
                            .childAtPosition(3)
                )
            ).atPosition(16).perform(click())
    }

    private fun selectCity() {
        onView(
            Matchers.allOf(
                withId(R.id.button), withText("Selecione"),
                withId(R.id.spCity)
                    .childAtPosition(0)
                    .childAtPosition(1)
            )
        ).perform(ViewActions.scrollTo(), click())

        onData(Matchers.anything())
            .inAdapterView(
                Matchers.allOf(
                    withId(R.id.list),
                    withClassName(
                        Matchers.`is`("android.widget.LinearLayout"))
                            .childAtPosition(3)
                )
            )
            .atPosition(0).perform(click())
    }

    private fun presetValidForm() {
        setField("teste@teste.com", R.id.edtEmail)
        setField("Teste Teste", R.id.edtUserName)
        setField("123456", R.id.edtPassword)
        setField("99 999999999", R.id.edtPhone)
    }

    private fun callButtonClick() {
        onView(withId(R.id.btnCreateAccount))
            .perform(isDisplayed().waitUntil())
            .perform(scrollTo())
            .perform(click())
    }

    private fun matchesNotIsDisplayed(idMessageError: Int) {
        onView(withText(idMessageError)).check(
            matches(not(isDisplayed())))
    }

    private fun matchesIsDisplayed(idMessageError: Int) {
        onView(withText(idMessageError)).check(matches(isDisplayed()))
    }
}