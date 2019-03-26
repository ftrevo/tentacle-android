package br.com.concrete.tentacle.features.myreservations

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.LoansListResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Test
import org.koin.standalone.inject

class MyReservationVMTest : BaseViewModelTest() {

    val myReservationViewModel: MyReservationViewModel by inject()

    @Test
    fun `when myReservationViewModel calls getHomeGames should return error message for 401`() {
        val expected =
            ViewStateModel<LoansListResponse>(
                status = ViewStateModel.Status.ERROR, model = null, errors = ErrorResponse()
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
        Assert.assertEquals(expected, actual)
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
                model = responseObject.data.list)
        var actual = ViewStateModel<LoansListResponse>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        myReservationViewModel.getMyReservations().observeForever {
            actual = it
        }
        myReservationViewModel.loadMyReservations()
        Assert.assertEquals(expected, actual)
    }
}