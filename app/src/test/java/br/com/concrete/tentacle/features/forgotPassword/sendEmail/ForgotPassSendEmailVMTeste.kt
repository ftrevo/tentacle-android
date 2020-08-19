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
import org.koin.standalone.inject

class ForgotPassSendEmailVMTeste : BaseViewModelTest() {

    private val forgotPassSendEmailViewModel: ForgotPassSendEmailViewModel by inject()

    @Test
    fun `when forgotPassSendEmailViewModel calls forgotPassword should return a user`() {

        // arrange
        val responseJson = getJson("mockjson/forgotPassword/sendEmail/user.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<User>>() {}.type
        val responseObject: BaseModel<User> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data
            )

        var actual = ViewStateModel<User>(status = ViewStateModel.Status.LOADING)
        forgotPassSendEmailViewModel.getStateModel().observeForever {
            actual = ViewStateModel(model = it.model, status = it.status)
        }

        // act
        forgotPassSendEmailViewModel.forgotPassword("email")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when forgotPassSendEmailViewModel calls forgotPassword should return a message not found`() {

        // arrange
        val responseJson = getJson("mockjson/errors/error_404.json")
        mockResponseError404()
        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.ERROR,
                model = null, errors = responseObject
            )

        var actual = ViewStateModel<User>(status = ViewStateModel.Status.LOADING)
        forgotPassSendEmailViewModel.getStateModel().observeForever {
            actual = ViewStateModel(model = it.model, status = it.status, errors = it.errors)
        }

        // act
        forgotPassSendEmailViewModel.forgotPassword("email")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when forgotPassSendEmailViewModel calls forgotPassword should return a message not found error 400`() {

        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.ERROR,
                model = null, errors = responseObject
            )

        var actual = ViewStateModel<User>(status = ViewStateModel.Status.LOADING)
        forgotPassSendEmailViewModel.getStateModel().observeForever {
            actual = ViewStateModel(model = it.model, status = it.status, errors = it.errors)
        }

        // act
        forgotPassSendEmailViewModel.forgotPassword("email")

        // assert
        assertEquals(expected, actual)
    }
}