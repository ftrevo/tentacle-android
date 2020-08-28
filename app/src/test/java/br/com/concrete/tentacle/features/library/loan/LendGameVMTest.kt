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
import org.junit.Assert
import org.junit.Test
import org.koin.standalone.inject

class LendGameVMTest : BaseViewModelTest() {

    private val lendGameViewModel: LendGameViewModel by inject()

    @Test
    fun `givenSuccessfulResponse whenFetchMediaLoan shouldReturnAMedia`() {
        // arrange
        val responseJson = getJson("mockjson/library/loan/lend_response_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<Media>>() {}.type
        val responseObject: BaseModel<Media> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data
        )
        var actual = ViewStateModel<Media>(status = ViewStateModel.Status.LOADING)
        lendGameViewModel.getMediaViewState().observeForever {
            actual = it
        }

        // act
        lendGameViewModel.fetchMediaLoan("someId")

        //assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenFetchMediaLoan shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        responseObject.statusCode = 400
        val expected = ViewStateModel<Media>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        var actual = ViewStateModel<Media>(status = ViewStateModel.Status.LOADING)
        lendGameViewModel.getMediaViewState().observeForever {
            actual = it
        }

        // act
        lendGameViewModel.fetchMediaLoan("someId")

        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessResponse whenUpdateMediaLoan shouldReturnALoanResponse`() {
        // arrange
        val responseJson = getJson("mockjson/library/loan/perform_loan_success.json")
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
        lendGameViewModel.getUpdateLoanViewState().observeForever {
            actual = it
        }

        // act
        lendGameViewModel.updateMediaLoan("someId", LoanActionRequest(LOAN_ACTION_LEND))

        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenPerformLoan shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        responseObject.statusCode = 400
        val expected = ViewStateModel<LoanResponse>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        var actual = ViewStateModel<LoanResponse>(status = ViewStateModel.Status.LOADING)
        lendGameViewModel.getUpdateLoanViewState().observeForever {
            actual = it
        }

        // act
        lendGameViewModel.updateMediaLoan("someId", LoanActionRequest(LOAN_ACTION_LEND))

        // assert
        Assert.assertEquals(expected, actual)
    }
}
