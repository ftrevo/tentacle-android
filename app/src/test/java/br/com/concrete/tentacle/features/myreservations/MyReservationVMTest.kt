package br.com.concrete.tentacle.features.myreservations

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.LoansListResponse
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanDeleteResponse
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.junit.Test
import org.koin.standalone.inject
import org.junit.Assert.assertEquals

class MyReservationVMTest : BaseViewModelTest() {

    val myReservationViewModel: MyReservationViewModel by inject()

    @Test
    fun `givenErrorResponse whenLoadMyReservations shouldReturnError400State`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<LoansListResponse>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        var actual = ViewStateModel<LoansListResponse>(status = ViewStateModel.Status.LOADING)
        myReservationViewModel.getMyReservations().observeForever {
            actual = it
        }

        // act
        myReservationViewModel.loadMyReservations()

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when myReservationViewModel calls getHomeGames should return success`() {
        // arrange
        val responseJson = getJson("mockjson/myreservations/load_my_reservations_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<LoansListResponse>>() {}.type
        val responseObject: BaseModel<LoansListResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data
        )
        var actual = ViewStateModel<LoansListResponse>(status = ViewStateModel.Status.LOADING)
        myReservationViewModel.getMyReservations().observeForever {
            actual = it
        }

        // act
        myReservationViewModel.loadMyReservations()

        // assert
        assertEquals(expected.model!!.list, actual.model!!.list)
        assertEquals(expected.status, actual.status)
        assertEquals(expected.errors, actual.errors)
        assertEquals(expected.filtering, actual.filtering)
    }

    @Test
    fun `givenSuccessfulReservationDeletion whensDeleteLoan shouldReturnSuccessState`() {
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
        myReservationViewModel.getStateDeleteLoan().observeForever {
            actual = it
        }

        // act
        myReservationViewModel.deleteLoan("id_loan")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenDeleteLoan shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<LoanDeleteResponse>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        var actual = ViewStateModel<LoanDeleteResponse>(status = ViewStateModel.Status.LOADING)
        myReservationViewModel.getStateDeleteLoan().observeForever {
            actual = it
        }

        // act
        myReservationViewModel.deleteLoan("id_loan")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenShowHistoryAsFalse whenMyReservations shouldReturnSuccessfulPath`() {
        // arrange
        val responseJson = getJson("mockjson/myreservations/load_my_reservations_success.json")
        mockResponse200(responseJson)
        val expectedPath = "/loans?mineOnly=true&limit=15&page=0&showHistory=false"
        val queryParameters = QueryParameters(showHistory = false)
        myReservationViewModel.myReservations(queryParameters)

        // act
        val requestedPath = mockServer.takeRequest().path

        // assert
        assertEquals(expectedPath, requestedPath)
    }

    @Test
    fun `givenShowHistoryAsTrue whenMyReservations shouldReturnSuccessfulPath`() {
        // arrange
        val responseJson = getJson("mockjson/myreservations/load_my_reservations_success.json")
        mockResponse200(responseJson)
        val expectedPath = "/loans?mineOnly=true&limit=15&page=0&showHistory=true"
        val queryParameters = QueryParameters(showHistory = true)
        myReservationViewModel.myReservations(queryParameters)

        // act
        val requestedPath = mockServer.takeRequest().path

        // assert
        assertEquals(expectedPath, requestedPath)
    }
}