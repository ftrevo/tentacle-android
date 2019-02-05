package br.com.concrete.tentacle.features.register

import android.app.Activity
import android.app.Instrumentation
import androidx.navigation.NavHost
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentNoActionBar
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterFragmentTest: BaseFragmentNoActionBar(){

    override fun setupFragment() {
        testFragment = RegisterFragment()
    }

    @Before
    fun setMock(){
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(getJson("mockjson/register/get_states_success.json"))
        )
    }

    @Test
    fun checkInvalidEmail() {
        presetValidForm()
        setFieldAndClickRegister("teste@teste", R.id.edtEmail)
        callButtonClick()
        matchesIsDisplayed(R.string.email_error)
    }
    @Test
    fun checkValidEmail() {
        presetValidForm()
        setFieldAndClickRegister("teste@teste.com", R.id.edtEmail)
        callButtonClick()
        matchesNotIsDisplayed(R.string.email_error)
    }

    //NAME
    @Test
    fun checkEmptyName() {
        presetValidForm()
        setFieldAndClickRegister("", R.id.edtUserName)
        callButtonClick()
        matchesIsDisplayed(R.string.name_error)
    }
    @Test
    fun checkInvalidName() {
        presetValidForm()
        setFieldAndClickRegister("", R.id.edtUserName)
        callButtonClick()
        matchesIsDisplayed(R.string.name_error)
    }
    @Test
    fun checkValidName() {
        presetValidForm()
        setFieldAndClickRegister("Teste Teste", R.id.edtUserName)
        callButtonClick()
        matchesNotIsDisplayed(R.string.name_error)
    }

    //PASSWORD
    @Test
    fun checkEmptyPassword() {
        presetValidForm()
        setFieldAndClickRegister("", R.id.edtPassword)
        callButtonClick()
        matchesIsDisplayed(R.string.password_error)
    }
    @Test
    fun checkInvalidPassword() {
        presetValidForm()
        setFieldAndClickRegister("123", R.id.edtPassword)
        callButtonClick()
        matchesIsDisplayed(R.string.password_error)
    }
    @Test
    fun checkValidPassword() {
        presetValidForm()
        setFieldAndClickRegister("123456", R.id.edtPassword)
        callButtonClick()
        matchesNotIsDisplayed(R.string.password_error)
    }

    //PHONE
    @Test
    fun checkEmptyPhone() {
        presetValidForm()
        setFieldAndClickRegister("", R.id.edtPhone)
        callButtonClick()
        matchesIsDisplayed(R.string.phone_error)
    }
    @Test
    fun checkInvalidPhone() {
        presetValidForm()
        setFieldAndClickRegister("999999999", R.id.edtPhone)
        callButtonClick()
        matchesIsDisplayed(R.string.phone_error)
    }
    @Test
    fun checkValidPhone() {
        presetValidForm()
        setFieldAndClickRegister("99 999999999", R.id.edtPhone)
        callButtonClick()
        matchesNotIsDisplayed(R.string.phone_error)
    }

    //State Spinner
    @Test
    fun checkEmptyStateSelection() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(getJson("mockjson/register/get_cities_success.json"))
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
                .setBody(getJson("mockjson/register/get_cities_success.json"))
        )

        presetValidForm()
        selectState()
        callButtonClick()
        matchesNotIsDisplayed(R.string.state_error)
    }

    //City Spinner
    @Test
    fun checkEmptyCitySelection() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(getJson("mockjson/register/get_cities_success.json"))
        )

        presetValidForm()
        selectState()
        callButtonClick()
        matchesIsDisplayed(R.string.city_error)
    }

    @Test
    fun checkValidCitySelection() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(getJson("mockjson/register/get_cities_success.json"))
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(getJson("mockjson/login/login_success.json"))
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(getJson("mockjson/home/load_home_games_success.json"))
        )

        presetValidForm()
        selectState()
        selectCity()
        callButtonClick()

        Intents.init()
        val matcher = allOf(hasComponent(NavHost::class.java.name))
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        intending(matcher).respondWith(result)
    }

    private fun selectState(){
        onView(
            Matchers.allOf(
                withId(R.id.button), withText("Selecione"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.spState),
                        0
                    ),
                    1
                )
            )
        ).perform(ViewActions.scrollTo(), click())
        onData(Matchers.anything())
            .inAdapterView(
                Matchers.allOf(
                    withId(R.id.list),
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        3
                    )
                )
            )
            .atPosition(16).perform(click())
    }

    private fun selectCity(){
        onView(
            Matchers.allOf(
                withId(R.id.button), withText("Selecione"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.spCity),
                        0
                    ),
                    1
                )
            )
        ).perform(ViewActions.scrollTo(), click())
        onData(Matchers.anything())
            .inAdapterView(
                Matchers.allOf(
                    withId(R.id.list),
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        3
                    )
                )
            )
            .atPosition(0).perform(click())
    }

    private fun setFieldAndClickRegister(textField: String, id: Int) {
        val textInputEditText = onView(
            Matchers.allOf(
                withId(R.id.edt),
                childAtPosition(
                    childAtPosition(
                        withId(id),
                        0
                    ),
                    1
                )
            )
        )
        textInputEditText.perform(
            ViewActions.scrollTo(),
            ViewActions.replaceText(textField)
        )
    }

    private fun presetValidForm() {
        setFieldAndClickRegister("teste@teste.com", R.id.edtEmail)
        setFieldAndClickRegister("Teste Teste", R.id.edtUserName)
        setFieldAndClickRegister("123456", R.id.edtPassword)
        setFieldAndClickRegister("99 999999999", R.id.edtPhone)
    }

    private fun callButtonClick() {
        onView(withId(R.id.btnCreateAccount)).perform(click())
    }

    private fun matchesNotIsDisplayed(idMessageError: Int) {
        onView(withText(idMessageError)).check(
            matches(not(isDisplayed())))
    }

    private fun matchesIsDisplayed(idMessageError: Int) {
        onView(withText(idMessageError)).check(matches(isDisplayed()))
    }
}