package br.com.concrete.tentacle.ui.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.features.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val emailInvalid = "email@"
private const val emailValid = "teste@email.com.br"
private const val passwordInvalid = "123"
private const val passwordValid = "123456"

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginUiTest {

    @get:Rule
    val mActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkEmptyEmailOnClickButton() {
        login {
            setEmail("")
            clickButtonLogin()
            matchesError(stringResource(R.string.verificar_campos_login, mActivityTestRule))
        }
    }

    @Test
    fun checkInvalidEmailOnClickButton() {
        login {
            setEmail(emailInvalid)
            clickButtonLogin()
            matchesError(stringResource(R.string.email_error, mActivityTestRule))
            matchesError(stringResource(R.string.verificar_campos_login, mActivityTestRule))
        }
    }

    @Test
    fun checkInvalidEmail() {
        login {
            setEmail(emailInvalid)
            matchesError(stringResource(R.string.email_error, mActivityTestRule))
        }
    }

    @Test
    fun checkValidEmailOnClickButton() {
        login {
            setEmail(emailValid)
            clickButtonLogin()
            matchWithoutTextError(stringResource(R.string.email_error, mActivityTestRule))
        }
    }

    @Test
    fun checkValidEmail() {
        login {
            setEmail(emailValid)
            matchWithoutTextError(stringResource(R.string.email_error, mActivityTestRule))
        }
    }

    @Test
    fun checkEmptyPasswordOnClickButton() {
        login {
            setPassword("")
            clickButtonLogin()
            matchesError(stringResource(R.string.verificar_campos_login, mActivityTestRule))
        }
    }

    @Test
    fun checkInvalidPasswordOnClickButton() {
        login {
            setPassword(passwordInvalid)
            clickButtonLogin()
            matchesError(stringResource(R.string.password_error, mActivityTestRule))
            matchesError(stringResource(R.string.verificar_campos_login, mActivityTestRule))
        }
    }

    @Test
    fun checkInvalidPassword() {
        login {
            setPassword(passwordInvalid)
            matchesError(stringResource(R.string.password_error, mActivityTestRule))
        }
    }

    @Test
    fun checkValidPassword() {
        login {
            setPassword(passwordValid)
            matchWithoutTextError(stringResource(R.string.password_error, mActivityTestRule))
        }
    }

    @Test
    fun checkValidPasswordOnClick() {
        login {
            setPassword(passwordValid)
            clickButtonLogin()
            matchWithoutTextError(stringResource(R.string.password_error, mActivityTestRule))
        }
    }

    @Test
    fun checkFieldsForm() {
        login {
            setEmail(emailValid)
            setPassword(passwordValid)
            clickButtonLogin()
        }
    }

    @Test
    fun clickRegisterAccount() {
        login {
            clickRegisterAccount()
        }
    }
}