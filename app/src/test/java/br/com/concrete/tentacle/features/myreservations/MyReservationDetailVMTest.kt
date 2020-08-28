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
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.standalone.inject

class MyReservationDetailVMTest : BaseViewModelTest() {

    private val myReservationDetail: MyReservationDetailViewModel by inject()

    @Test
    fun `givenReservationSuccess whenLoadMyLoan shouldReturnDetailedInformation`() {
        // arrange
        val responseJson = getJson("mockjson/myreservations/myreservations_detail_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<LoanResponse>>() {}.type
        val responseObject: BaseModel<LoanResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
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
    fun `givenError400Response whenLoadMyLoan shouldReturnError400State`() {
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
    fun `givenSuccessfulResponse whenDeleteLoan shouldReturnSuccessState`() {
        // arrange
        val responseJson = getJson("mockjson/myreservations/delete_my_reservation_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<LoanDeleteResponse>>() {}.type
        val responseObject: BaseModel<LoanDeleteResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data
        )
        var actual = ViewStateModel<LoanDeleteResponse>(status = ViewStateModel.Status.LOADING)
        myReservationDetail.getStateDeleteLoan().observeForever {
            actual = it
        }

        // act
        myReservationDetail.deleteLoan("id_loan")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenError400Response whenDeleteLoan shouldReturnError400State`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<LoanDeleteResponse>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        var actual = ViewStateModel<LoanDeleteResponse>(status = ViewStateModel.Status.LOADING)
        myReservationDetail.getStateDeleteLoan().observeForever {
            actual = it
        }

        // act
        myReservationDetail.deleteLoan("id_loan")

        // assert
        assertEquals(expected, actual)
    }
}