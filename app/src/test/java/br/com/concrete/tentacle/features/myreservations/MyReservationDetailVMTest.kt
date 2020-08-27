package br.com.concrete.tentacle.features.myreservations

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanDeleteResponse
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.features.myreservations.detail.MyReservationDetailViewModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.standalone.inject

class MyReservationDetailVMTest : BaseViewModelTest() {

    private val myReservationDetail: MyReservationDetailViewModel by inject()

    @Test
    fun `when MyReservationDetailViewModel calls loadMyLoan should return the details of reservations with success`() {
        // arrange
        val responseJson = getJson("mockjson/myreservations/myreservations_detail_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<LoanResponse>>() {}.type
        val responseObject: BaseModel<LoanResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data
            )
        var actual = ViewStateModel<LoanResponse>(status = ViewStateModel.Status.LOADING)
        myReservationDetail.getViewState().observeForever {
            actual = it
        }

        // act
        myReservationDetail.loadMyLoan("id")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when MyReservationDetailViewModel calls loadMyLoan should return message for 400`() {
        // arrange
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )
        mockResponseError400()
        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)
        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.ERROR,
                model = null,
                errors = responseObject
            )
        var actual = ViewStateModel<LoanResponse>(status = ViewStateModel.Status.LOADING)
        myReservationDetail.getViewState().observeForever {
            actual = it
        }

        // act
        myReservationDetail.loadMyLoan("id")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when myReservationDetail calls delete should return success`() {
        // arrange
        val responseJson = getJson(
            "mockjson/myreservations/delete_my_reservation_success.json"
        )
        mockResponse200(responseJson)
        val collectionType = object : com.google.common.reflect.TypeToken<BaseModel<LoanDeleteResponse>>() {}.type
        val responseObject: BaseModel<LoanDeleteResponse> =
            GsonBuilder().create().fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data)
        var actual = ViewStateModel<LoanDeleteResponse>(status = ViewStateModel.Status.LOADING)
        myReservationDetail.getStateDeleteLoan().observeForever {
            actual = it
        }

        // act
        myReservationDetail.deleteLoan("id_loan")

        // assert
        assertEquals(expected.model, actual.model)
        assertEquals(expected.status, actual.status)
        assertEquals(expected.errors, actual.errors)
        assertEquals(expected.filtering, actual.filtering)
    }

    @Test
    fun `when myReservationDetail calls delete should return error message for 400`() {
        // arrange
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<LoanDeleteResponse>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)
        var actual = ViewStateModel<LoanDeleteResponse>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        myReservationDetail.getStateDeleteLoan().observeForever {
            actual = it
        }

        // act
        myReservationDetail.deleteLoan("id_loan")

        // assert
        assertEquals(expected, actual)
    }
}