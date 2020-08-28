package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.koin.standalone.inject

class ForgotPassSendEmailVMTest : BaseViewModelTest() {

    private val forgotPassSendEmailViewModel: ForgotPassSendEmailViewModel by inject()
    lateinit var actual: ViewStateModel<User>

    @BeforeEach
    fun initializeValidationVariable(){
        actual = ViewStateModel(status = ViewStateModel.Status.LOADING)
    }

    @Test
    fun `givenSuccessResponse whenForgotPassword shouldReturnUser`() {
        // arrange
        val responseJson = getJson("mockjson/forgotPassword/sendEmail/user.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<User>>() {}.type
        val responseObject: BaseModel<User> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data
        )
        forgotPassSendEmailViewModel.getStateModel().observeForever {
            actual = ViewStateModel(
                model = it.model,
                status = it.status
            )
        }

        // act
        forgotPassSendEmailViewModel.forgotPassword("email")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenError404Response whenForgotPassword shouldReturnError404Status`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_404.json")
        mockResponseError404()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        forgotPassSendEmailViewModel.getStateModel().observeForever {
            actual = ViewStateModel(
                model = it.model,
                status = it.status,
                errors = it.errors
            )
        }

        // act
        forgotPassSendEmailViewModel.forgotPassword("email")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenError400Response whenForgotPassword shouldReturnError400Status`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        forgotPassSendEmailViewModel.getStateModel().observeForever {
            actual = ViewStateModel(
                model = it.model,
                status = it.status,
                errors = it.errors
            )
        }

        // act
        forgotPassSendEmailViewModel.forgotPassword("email")

        // assert
        assertEquals(expected, actual)
    }
}