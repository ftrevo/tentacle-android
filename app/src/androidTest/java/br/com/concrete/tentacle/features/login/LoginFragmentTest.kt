package br.com.concrete.tentacle.features.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentNoActionBarNoBottomBarTest
import br.com.concrete.tentacle.extensions.getJson
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers
import org.junit.Test
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.mockwebserver.Dispatcher

class LoginFragmentTest : BaseFragmentNoActionBarNoBottomBarTest() {

    override fun setupFragment() {
        testFragment = LoginFragment()
    }

    @Test
    fun checkInvalidEmail() {
        setField("teste@teste", R.id.edtEmail)
        callButtonClick()
        matchesIsDisplayed(R.string.email_error)
    }

    @Test
    fun checkValidEmail() {
        setField("teste@teste.com", R.id.edtEmail)
        callButtonClick()
        matchesNotIsDisplayed(R.string.email_error)
    }

    @Test
    fun checkPasswordEmpty() {
        setField("teste@test.com", R.id.edtEmail)
        setField("", R.id.edtPassword)
        callButtonClick()
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.verificar_campos_login)))
    }

    @Test
    fun checkEmailEmpty() {
        setField("", R.id.edtEmail)
        setField("123456", R.id.edtPassword)
        callButtonClick()
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.verificar_campos_login)))
    }

    @Test
    fun loginSuccess() {
        mockWebServer.setDispatcher(getDispatcher())
        setField("teste@test.com", R.id.edtEmail)
        setField("123456", R.id.edtPassword)
        callButtonClick()
        checkIfGoesToHome()
    }

    @Test
    fun loginError400() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("mockjson/errors/error_400.json".getJson())
        )

        setField("teste@test.com", R.id.edtEmail)
        setField("123456", R.id.edtPassword)
        callButtonClick()
        onView(withText("ERROR MESSAGE.")).check(matches(isDisplayed()))
    }

    @Test
    fun loginError401() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(401)
        )

        setField("teste@test.com", R.id.edtEmail)
        setField("123456", R.id.edtPassword)
        callButtonClick()
        onView(withText("Usuário ou senha inválidos!")).check(matches(isDisplayed()))
    }

    @Test
    fun checkInvalidPassword() {
        setField("123", R.id.edtPassword)
        callButtonClick()
        matchesIsDisplayed(R.string.password_error)
    }

    @Test
    fun checkValidPassword() {
        setField("123456", R.id.edtPassword)
        callButtonClick()
        matchesNotIsDisplayed(R.string.password_error)
    }

    @Test
    fun loginError426() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(426)
                .setBody("mockjson/errors/error_400.json".getJson())
        )

        setField("teste@test.com", R.id.edtEmail)
        setField("123456", R.id.edtPassword)
        callButtonClick()
        onView(withText("ERROR MESSAGE.")).check(matches(isDisplayed()))
    }

    private fun callButtonClick() {
        Espresso.onView(ViewMatchers.withId(R.id.btLogin)).perform(ViewActions.click())
    }

    private fun matchesNotIsDisplayed(idMessageError: Int) {
        Espresso.onView(ViewMatchers.withText(idMessageError)).check(
            ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed()))
        )
    }

    private fun matchesIsDisplayed(idMessageError: Int) {
        Espresso.onView(ViewMatchers.withText(idMessageError)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun getDispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                var mockResponse: MockResponse = MockResponse().setResponseCode(404)
                request?.let {
                    when {
                        request.path == "/login" -> {
                            mockResponse = MockResponse()
                                .setResponseCode(200)
                                .setBody("mockjson/login/login_success.json".getJson())
                        }
                        request.path == "/refresh-token" -> {
                            mockResponse =
                                MockResponse()
                                    .setResponseCode(200)
                                    .setBody("mockjson/token/token_update_success.json".getJson())
                        }
                        request.path == "/users/profile" -> {
                            mockResponse =
                                MockResponse()
                                    .setResponseCode(200)
                                    .setBody("mockjson/profile/get_profile_success.json".getJson())
                        }
                        request.path == "/library/home" -> {
                            mockResponse =
                                MockResponse()
                                    .setResponseCode(200)
                                    .setBody("mockjson/home/load_home_games_success.json".getJson())
                        }
                    }
                }
                return mockResponse
            }
        }
    }
}