package br.com.concrete.tentacle.viewmodel

import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.login.LoginViewModel
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.standalone.inject

class LoginVMTest : BaseViewModelTest() {

    private val loginViewMock: LoginViewModel by inject()
    lateinit var actual: ViewStateModel<Session>

    @Before
    fun initializeValidationVariableAsLoading() {
        actual = ViewStateModel(status = ViewStateModel.Status.LOADING)
    }

    @Test
    fun `givenError401Response whenLogin shouldReturnError401Message`() {
        // arrange
        mockResponseError401()
        val error = ErrorResponse().apply {
            statusCode = 401
            messageInt.add(R.string.user_or_password_error)
        }
        val expected = ViewStateModel<Session>(
            status = ViewStateModel.Status.ERROR,
            model = null, errors = error
        )
        loginViewMock.getStateModel().observeForever {
            actual = it.peekContent()
        }

        // act
        loginViewMock.loginUser("daivid.v.leal@concrete.com.br", "123456")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenLogin shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        mockResponseError400()
        val expected = ViewStateModel<Session>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        loginViewMock.getStateModel().observeForever {
            actual = it.peekContent()
        }

        // act
        loginViewMock.loginUser("daivid.v.leal@concrete.com.br", "123456")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessResponse whenLogin shouldReturnSuccessState`() {
        // arrange
        val responseJson = getJson("mockjson/login/login_success.json")
        mockResponse200(responseJson)
        mockResponse200(getJson("mockjson/token/token_update_success.json"))
        val collectionType = object : TypeToken<BaseModel<Session>>() {}.type
        val responseObject: BaseModel<Session> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data
        )
        loginViewMock.getStateModel().observeForever {
            actual = it.peekContent()
        }

        // act
        loginViewMock.loginUser("daivid.v.leal@concrete.com.br", "123456")

        // assert
        assertEquals(expected, actual)
    }
}