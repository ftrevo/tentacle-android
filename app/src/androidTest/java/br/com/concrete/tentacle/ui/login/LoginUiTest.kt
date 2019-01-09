package br.com.concrete.tentacle.ui.login

import android.app.Instrumentation
import androidx.lifecycle.MutableLiveData
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.features.MainActivity
import br.com.concrete.tentacle.features.login.LoginViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


private const val emailInvalid = "email@"
private const val emailValid = "teste@email.com.br"
private const val passwordInvalid = "123"
private const val passwordValid = "123456"


@LargeTest
@RunWith(MockitoJUnitRunner::class)
class LoginUiTest: Instrumentation() {

    @get:Rule
    val mActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Mock
    lateinit var sharedPrefRepository: SharedPrefRepository

    @Mock
    lateinit var loginRepository: LoginRepository

    lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp(){
        loginViewModel = LoginViewModel(loginRepository, sharedPrefRepository)
    }

    @Test
    fun checkShouldDisplayInitialState() {
        login {
            matchDisplayedMessageOla()
            matchDisplayedMessage()
            matchDisplayedTentacleEditTextEmail()
            matchDisplayedTentacleEditTextPassword()
            matchDisplayedButtonLogin()
            matchDisplayedTextRegisterAccount()
        }
    }

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
            matchesNotError(stringResource(R.string.email_error, mActivityTestRule))
        }
    }

    @Test
    fun checkValidEmail() {
        login {
            setEmail(emailValid)
            matchesNotError(stringResource(R.string.email_error, mActivityTestRule))
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
            matchesNotError(stringResource(R.string.password_error, mActivityTestRule))
        }
    }

    @Test
    fun checkValidPasswordOnClick() {
        login {
            setPassword(passwordValid)
            clickButtonLogin()
            matchesNotError(stringResource(R.string.password_error, mActivityTestRule))
        }
    }

    @Test
    fun checkFieldsForm() {
        login {
            setEmail(emailValid)
            setPassword(passwordValid)
            clickButtonLogin()
            matchesNotErrorSnackBar("Por favor, verifique os campos obrigatórios.")
        }
    }

    @Test
    fun clickRegisterAccount() {
        login {
            clickRegisterAcc()
        }
    }

    @Test
    fun testLogin() {
        val live = MutableLiveData<ViewStateModel<Session>>()
        loginViewModel.getStateModel()
        Mockito.`when`(loginViewModel.getStateModel())
            .then {
                live.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = Session("", "", "")))
            }
    }

    @Test
    fun testLoginError() {
        val live = MutableLiveData<ViewStateModel<Session>>()
        loginViewModel.getStateModel()
        Mockito.`when`(loginViewModel.getStateModel())
            .then {
                live.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, errors = ErrorResponse()))
            }
    }


}