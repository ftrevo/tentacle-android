package br.com.concrete.tentacle.features.library.loan

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import org.junit.Assert
import org.junit.Test
import org.koin.standalone.inject

class LoanVMTest : BaseViewModelTest() {

    private val loanViewModel: LoanViewModel by inject()

    @Test
    fun `givenSuccessfulResponse whenGetLibrary shouldReturnLibrary`() {
        // arrange
        val responseJson = getJson("mockjson/library/get_one_library_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<Library>>() {}.type
        val responseObject: BaseModel<Library> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data
        )
        var actual = ViewStateModel<Library>(status = ViewStateModel.Status.LOADING)
        loanViewModel.getLibrary().observeForever {
            actual = it
        }

        // act
        loanViewModel.loadLibrary("someId")

        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenLoadLibrary shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        responseObject.statusCode = 400
        val expected = ViewStateModel<Library>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        var actual = ViewStateModel<Library>(status = ViewStateModel.Status.LOADING)
        loanViewModel.getLibrary().observeForever {
            actual = it
        }

        // act
        loanViewModel.loadLibrary("someId")

        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessfulResponse whenPerformLoan shouldReturnLoanResponse`() {
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
        loanViewModel.getLoan().observeForever {
            actual = it
        }

        // act
        loanViewModel.performLoad("someId")

        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `GivenErrorResponse whenPerformLoan shouldReturnErrorState`() {
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
        loanViewModel.getLoan().observeForever {
            actual = it
        }

        // act
        loanViewModel.performLoad("someId")

        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessfulResponse whenGetGame shouldReturnGameDetail`() {
        // arrange
        val responseJson = getJson("mockjson/registerMedia/detail_game_success.json")
        mockResponse200(responseJson)
        var actualDetail = ViewStateModel<Game>(
            status = ViewStateModel.Status.LOADING
        )
        val classType = object : TypeToken<BaseModel<Game>>() {}.type
        val baseResponse: BaseModel<Game> = GsonBuilder()
            .create()
            .fromJson(responseJson, classType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = baseResponse.data
        )
        loanViewModel.getGame().observeForever {
            actualDetail = it
        }

        // act
        loanViewModel.getDetailsGame("idGame")

        // assert
        Assert.assertEquals(expected, actualDetail)
    }
}
