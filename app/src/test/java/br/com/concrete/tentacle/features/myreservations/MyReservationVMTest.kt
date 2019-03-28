package br.com.concrete.tentacle.features.myreservations

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.LoansListResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanDeleteResponse
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Test
import org.koin.standalone.inject
import org.junit.Assert.assertEquals

class MyReservationVMTest : BaseViewModelTest() {

    val myReservationViewModel: MyReservationViewModel by inject()

    @Test
    fun `when myReservationViewModel calls getHomeGames should return error message for 401`() {
        val expected =
            ViewStateModel<LoansListResponse>(
                status = ViewStateModel.Status.ERROR, model = null, errors = ErrorResponse(statusCode = 401)
            )
        var actual = ViewStateModel<LoansListResponse>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(401)

        mockServer.enqueue(mockResponse)

        myReservationViewModel.getMyReservations().observeForever {
            actual = it
        }
        myReservationViewModel.loadMyReservations()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when myReservationViewModel calls getHomeGames should return error message for 400`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<LoansListResponse>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)
        var actual = ViewStateModel<LoansListResponse>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        myReservationViewModel.getMyReservations().observeForever {
            actual = it
        }
        myReservationViewModel.loadMyReservations()
        assertEquals(expected, actual)
    }

    @Test
    fun `when myReservationViewModel calls getHomeGames should return success`() {
        val responseJson = getJson(
            "mockjson/myreservations/load_my_reservations_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<LoansListResponse>>() {}.type
        val responseObject: BaseModel<LoansListResponse> =
            GsonBuilder().create().fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data)
        var actual = ViewStateModel<LoansListResponse>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        myReservationViewModel.getMyReservations().observeForever {
            actual = it
        }
        myReservationViewModel.loadMyReservations()
        assertEquals(expected.model!!.list, actual.model!!.list)
        assertEquals(expected.status, actual.status)
        assertEquals(expected.errors, actual.errors)
        assertEquals(expected.filtering, actual.filtering)
    }

    @Test
    fun `when myReservationViewModel calls delete should return success`() {
        val responseJson = getJson(
            "mockjson/myreservations/delete_my_reservation_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<LoanDeleteResponse>>() {}.type
        val responseObject: BaseModel<LoanDeleteResponse> =
            GsonBuilder().create().fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data)
        var actual = ViewStateModel<LoanDeleteResponse>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        myReservationViewModel.getStateDeleteLoan().observeForever {
            actual = it
        }
        myReservationViewModel.deleteLoan("id_loan")
        assertEquals(expected.model, actual.model)
        assertEquals(expected.status, actual.status)
        assertEquals(expected.errors, actual.errors)
        assertEquals(expected.filtering, actual.filtering)
    }

    @Test
    fun `when myReservationViewModel calls delete should return error message for 401`() {
        val expected =
            ViewStateModel<LoanDeleteResponse>(
                status = ViewStateModel.Status.ERROR, model = null, errors = ErrorResponse(statusCode = 401)
            )
        var actual = ViewStateModel<LoanDeleteResponse>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(401)

        mockServer.enqueue(mockResponse)

        myReservationViewModel.getStateDeleteLoan().observeForever {
            actual = it
        }
        myReservationViewModel.deleteLoan("id_loan")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when myReservationViewModel calls delete should return error message for 400`() {
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

        myReservationViewModel.getStateDeleteLoan().observeForever {
            actual = it
        }
        myReservationViewModel.deleteLoan("id_loan")
        assertEquals(expected, actual)
    }
}