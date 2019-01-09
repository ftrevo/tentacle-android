package br.com.concrete.tentacle.viewmodel

import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.login.LoginViewModel
import br.com.concrete.tentacle.mock.baseModelLoginSuccess
import br.com.concrete.tentacle.mock.error400
import br.com.concrete.tentacle.mock.error401
import br.com.concrete.tentacle.mock.errorResponse
import br.com.concrete.tentacle.repositories.LoginRepository
import br.com.concrete.tentacle.repositories.SharedPrefRepository
import io.reactivex.Flowable
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class LoginVMTest : BaseViewModelTest() {

    @Mock
    lateinit var sharePrefRepository: SharedPrefRepository

    @Mock
    lateinit var loginRepository: LoginRepository

    private lateinit var loginViewModelTest: LoginViewModel

    @Before
    fun before() {
        loginViewModelTest = LoginViewModel(loginRepository, sharePrefRepository)
    }

    @Test
    fun loginUserVMTestSuccess() {
        val expected = ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModelLoginSuccess.data)
        var actual = ViewStateModel<Session>(status = ViewStateModel.Status.LOADING)

        Mockito.`when`(loginRepository.loginUser("email@email.com", "123456"))
            .thenReturn(Flowable.just(baseModelLoginSuccess))
        val result = loginViewModelTest.getStateModel()
        result.observeForever {
            actual = it
        }
        loginViewModelTest.loginUser("email@email.com", "123456")
        assertEquals(expected, actual)
    }

    @Test
    fun testLoginErrorMockito() {
        val expected = ViewStateModel<Session>(status = ViewStateModel.Status.ERROR, errors = errorResponse)
        var actual = ViewStateModel<Session>(status = ViewStateModel.Status.LOADING)

        Mockito.`when`(loginRepository.loginUser("email@email.com", "123456"))
            .thenReturn(Flowable.error(error400))

        val result = loginViewModelTest.getStateModel()
        result.observeForever {
            actual = it
        }

        loginViewModelTest.loginUser("email@email.com", "123456")
        assertEquals(expected, actual)
    }

    @Test
    fun testLoginErrorMockito401() {
        errorResponse.messageInt.add(R.string.user_or_password_error)

        val expected = ViewStateModel<Session>(status = ViewStateModel.Status.ERROR, errors = errorResponse)
        var actual = ViewStateModel<Session>(status = ViewStateModel.Status.LOADING)

        Mockito.`when`(loginRepository.loginUser("email@email.com", "123456"))
            .thenReturn(Flowable.error(error401))

        val result = loginViewModelTest.getStateModel()
        result.observeForever {
            actual = it
        }

        loginViewModelTest.loginUser("email@email.com", "123456")
        assertEquals(expected, actual)
    }
}