package br.com.concrete.tentacle.features.forgotPassword.passwordRecovery

import androidx.test.espresso.intent.rule.IntentsTestRule
import br.com.concrete.tentacle.testing.SingleFragmentTestActivity
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PasswordRecoveryFragmentTest {

    var mockWebServer = MockWebServer()

    @get:Rule
    val intentsTestRule = IntentsTestRule(SingleFragmentTestActivity::class.java)

    @Before
    fun startServer() {
        mockWebServer.start(8080)
    }

    @After
    fun shutdownServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenFragmentStartWithNoExtras_whenStartFragment_shouldDisplayItCorrectly() {
        arrange {
            initFragment(intentsTestRule)
        }
        assert {
            isEmailTextEmpty()
            isTokenEmpty()
            isPasswordEmpty()
            isConfirmPasswordEmpty()
        }
    }

    @Test
    fun givenEmailPassedAsParameter_whenStartFragment_shouldDisplayItCorrectly() {
        arrange {
            initFragmentWithEmailParameter(intentsTestRule)
        }
        assert {
            isEmailTextWithCorrectContent()
            isTokenEmpty()
            isPasswordEmpty()
            isConfirmPasswordEmpty()
        }
    }

    @Test
    fun givenEmptyEmailField_whenRecoveringPassword_shouldDisplayNotValidEmailError() {
        arrange {
            initFragment(intentsTestRule)
        }
        act {
            typeToken()
            typeCorrectPassword()
            typeCorrectPasswordConfirmation()
            clickRecoveryButton()
        }
        assert {
            emailFieldErrorIsDisplayed()
        }
    }

    @Test
    fun givenEmptyTokenField_whenRecoveringaPassword_shouldDisplayNotValidTokenError() {
        arrange {
            initFragment(intentsTestRule)
        }
        act {
            typeEmail()
            typeCorrectPassword()
            typeCorrectPasswordConfirmation()
            clickRecoveryButton()
        }
        assert {
            tokenFieldErrorIsDisplayed()
        }
    }

    @Test
    fun givenEmptyPasswordField_whenRecoveringaPassword_shouldDisplayNotValidPasswordError() {
        arrange {
            initFragment(intentsTestRule)
        }
        act {
            typeEmail()
            typeToken()
            clickRecoveryButton()
        }
        assert {
            newPassFieldErrorIsDisplayed()
        }
    }

    @Test
    fun givenEmptyPasswordConfirmationField_whenRecoveringaPassword_shouldDisplayNotValidPasswordConfirmationError() {
        arrange {
            initFragment(intentsTestRule)
        }
        act {
            typeEmail()
            typeToken()
            typeCorrectPassword()
            clickRecoveryButton()
        }
        assert {
            confirmPassFieldErrorIsDisplayed()
        }
    }

    @Test
    fun givenShortPassword_whenRecoveringPassword_shouldDisplayErrorMessage() {
        arrange {
            initFragment(intentsTestRule)
        }
        act {
            typeEmail()
            typeToken()
            typeShortPassword()
            clickRecoveryButton()
        }
        assert {
            newPassFieldErrorIsDisplayed()
            shortPasswordError()
        }
    }

    @Test
    fun givenCorrectPasswordAndShortPasswordConfirmation_whenRecoveringPassword_shouldDisplayConfirmationFieldError() {
        arrange {
            initFragment(intentsTestRule)
        }
        act {
            typeEmail()
            typeToken()
            typeCorrectPassword()
            typeShortPasswordConfirmation()
            clickRecoveryButton()
        }
        assert {
            confirmPassFieldErrorIsDisplayed()
            shortConfirmationPasswordError()
        }
    }

    @Test
    fun givenDifferentPasswordInputs_whenRecoveringPassword_shouldShowError() {
        arrange {
            initFragment(intentsTestRule)
        }
        act {
            typeEmail()
            typeToken()
            typeCorrectPassword()
            typeWrongPasswordConfirmation()
            clickRecoveryButton()
        }
        assert {
            confirmPassFieldErrorIsDisplayed()
            differentPasswordsError()
        }
    }

    @Test
    fun givenInvalidToken_whenRecoveringPassword_shouldDisplayErrorMessage() {
        arrange {
            initFragment(intentsTestRule)
            mockErrorResponse(mockWebServer)
        }
        act {
            typeEmail()
            typeToken()
            typeCorrectPassword()
            typeCorrectPasswordConfirmation()
            clickRecoveryButton()
        }
        assert {
            genericResponseErrorIsDisplayed()
        }
    }
}
