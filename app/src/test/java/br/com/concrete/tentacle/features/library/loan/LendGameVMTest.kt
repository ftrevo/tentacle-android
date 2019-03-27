package br.com.concrete.tentacle.features.library.loan

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.LoanActionRequest
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.features.lendgame.LendGameViewModel
import br.com.concrete.tentacle.utils.LOAN_ACTION_LEND
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Test
import org.koin.standalone.inject

class LendGameVMTest : BaseViewModelTest() {

    private val lendGameViewModel: LendGameViewModel by inject()

    @Test
    fun `when loanViewModel calls fetchMediaLoan should return a Media`() {

        val responseJson = getJson(
            "mockjson/library/loan/lend_response_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<Media>>() {}.type
        val responseObject: BaseModel<Media> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data)

        var actual = ViewStateModel<Media>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        lendGameViewModel.getMediaViewState().observeForever {
            actual = it
        }

        lendGameViewModel.fetchMediaLoan("someId")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when lendGameViewModel calls fetchMediaLoan should return error message for 401`() {
        val expected =
            ViewStateModel<Media>(
                status = ViewStateModel.Status.ERROR, model = null, errors = ErrorResponse(statusCode = 401)
            )
        var actual = ViewStateModel<Media>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(401)

        mockServer.enqueue(mockResponse)

        lendGameViewModel.getMediaViewState().observeForever {
            actual = it
        }

        lendGameViewModel.fetchMediaLoan("someId")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when lendGameViewModel calls fetchMediaLoan should return error message for 400`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)
        responseObject.statusCode = 400
        val expected =
            ViewStateModel<Media>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)
        var actual = ViewStateModel<Media>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        lendGameViewModel.getMediaViewState().observeForever {
            actual = it
        }

        lendGameViewModel.fetchMediaLoan("someId")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when lendGameViewModel calls updateMediaLoan should return a LoanResponse`() {

        val responseJson = getJson(
            "mockjson/library/loan/perform_loan_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<LoanResponse>>() {}.type
        val responseObject: BaseModel<LoanResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data)

        var actual = ViewStateModel<LoanResponse>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        lendGameViewModel.getUpdateLoanViewState().observeForever {
            actual = it
        }

        lendGameViewModel.updateMediaLoan("someId", LoanActionRequest(LOAN_ACTION_LEND))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when lendGameViewModel calls updateMediaLoan should return error message for 401`() {
        val expected =
            ViewStateModel<LoanResponse>(
                status = ViewStateModel.Status.ERROR, model = null, errors = ErrorResponse(statusCode = 401)
            )
        var actual = ViewStateModel<LoanResponse>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(401)

        mockServer.enqueue(mockResponse)

        lendGameViewModel.getUpdateLoanViewState().observeForever {
            actual = it
        }

        lendGameViewModel.updateMediaLoan("someId", LoanActionRequest(LOAN_ACTION_LEND))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when lendGameViewModel calls performLoan should return error message for 400`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)
        responseObject.statusCode = 400
        val expected =
            ViewStateModel<LoanResponse>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)
        var actual = ViewStateModel<LoanResponse>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        lendGameViewModel.getUpdateLoanViewState().observeForever {
            actual = it
        }

        lendGameViewModel.updateMediaLoan("someId", LoanActionRequest(LOAN_ACTION_LEND))
        Assert.assertEquals(expected, actual)
    }
}
