package br.com.concrete.tentacle.features.myreservations

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
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
        val responseJson = getJson("mockjson/myreservations/myreservations_detail_success.json")

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

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        myReservationDetail.getViewState().observeForever {
            actual = it
        }

        myReservationDetail.loadMyLoan("id")
        assertEquals(expected, actual)
    }

    @Test
    fun `when MyReservationDetailViewModel calls loadMyLoan should return message for 401`() {
        val expected =
                ViewStateModel(
                    status = ViewStateModel.Status.ERROR,
                    model = null,
                    errors = ErrorResponse()
                )

        var actual = ViewStateModel<LoanResponse>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(401)

        mockServer.enqueue(mockResponse)

        myReservationDetail.getViewState().observeForever {
            actual = it
        }

        myReservationDetail.loadMyLoan("id")
        assertEquals(expected, actual)
    }

    @Test
    fun `when MyReservationDetailViewModel calls loadMyLoan should return message for 400`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )
        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
                ViewStateModel(
                    status = ViewStateModel.Status.ERROR,
                    model = null,
                    errors = responseObject
                )

        var actual = ViewStateModel<LoanResponse>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        myReservationDetail.getViewState().observeForever {
            actual = it
        }

        myReservationDetail.loadMyLoan("id")
        assertEquals(expected, actual)
    }
}