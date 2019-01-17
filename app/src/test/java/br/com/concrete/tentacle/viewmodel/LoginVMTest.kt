package br.com.concrete.tentacle.viewmodel

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.features.login.LoginViewModel
import org.junit.Test
import org.koin.standalone.inject
import org.mockito.Mock

class LoginVMTest : BaseViewModelTest() {

    @Mock
    lateinit var loginRepository: LoginRepository

    private val loginViewModelTest: LoginViewModel by inject()

    @Test
    fun loginUserVMTestSuccess() {

    }

    @Test
    fun testLoginErrorMockito() {

    }

    @Test
    fun testLoginErrorMockito401() {

    }
}