package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import androidx.test.espresso.intent.rule.IntentsTestRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForgotPassSendEmailTest {

    val mockWebServer = MockWebServer()

    @get:Rule
    var activityTestRule = IntentsTestRule(ForgotPassSendEmailActivity::class.java)

    @Before
    fun startServer() {
        mockWebServer.start(8080)
    }

    @After
    fun shutdownServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenInvalidEmail_whenClickSendButton_shouldDisplayInvalidEmailError() {
        act {
            typeInvalidEmail()
            clickSendButton()
        }
        assert {
            isInvalidEmailErrorDisplayed()
        }
    }

    @Test
    fun givenSuccessfulResponse_whenClickSendButton_shouldGoToPasswordRecoveryActivity() {
        arrange {
            mockSuccessResponse(mockWebServer)
        }
        act {
            typeValidEmail()
            clickSendButton()
        }
        assert {
            checkGoToPassRecovery()
        }
    }

    @Test
    fun givenErrorResponse_whenClickSendButton_shouldShowErrorDialog() {
        arrange {
            mockErrorResponse(mockWebServer)
        }
        act {
            typeValidEmail()
            clickSendButton()
        }
        assert {
            showGenericError()
        }
    }

    @Test
    fun givenTokenAlreadyReceived_whenClickTokenText_shouldGoToPasswordRecoveryActivity() {
        act {
            clickTokenText()
        }
        assert {
            checkGoToPassRecovery()
        }
    }
}