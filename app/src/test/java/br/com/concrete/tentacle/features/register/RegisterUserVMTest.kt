package br.com.concrete.tentacle.viewmodel

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.CityResponse
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.StateResponse
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.register.RegisterUserViewModel
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.standalone.inject

class RegisterUserVMTest : BaseViewModelTest() {

    private val registerUserViewModelTest: RegisterUserViewModel by inject()

    @Test
    fun `givenSuccessfulResponse whenLoadCities shouldReturnListOfCities`() {
        // arrange
        val responseJson = getJson("mockjson/user/get_cities_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<CityResponse>>() {}.type
        val responseObject: BaseModel<CityResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.cities
        )
        var actual = ViewStateModel<ArrayList<String>>(status = ViewStateModel.Status.LOADING)
        registerUserViewModelTest.getCities().observeForever {
            actual = it
        }
        val stateId = "5a107a691592ca19f7b4a860"

        // act
        registerUserViewModelTest.loadCities(stateId)

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when registerUserViewModel calls loadStates should return a list of states`() {
        // arrange
        val responseJson = getJson("mockjson/user/get_states_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<StateResponse>>() {}.type
        val responseObject: BaseModel<StateResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        var actual = ViewStateModel<ArrayList<State>>(status = ViewStateModel.Status.LOADING)
        registerUserViewModelTest.getStates().observeForever {
            actual = it
        }

        // act
        registerUserViewModelTest.loadStates()

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenValidUser whenRegisterUser shouldReturnSuccessState`() {
        // arrange
        val responseJson = getJson("mockjson/login/login_success.json")
        mockResponse200(responseJson)
        mockResponse200(getJson("mockjson/token/token_update_success.json"))
        val collectionType = object : TypeToken<BaseModel<Session>>() {}.type
        val responseObject: BaseModel<Session> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data
        )
        var actual = ViewStateModel<Session>(status = ViewStateModel.Status.LOADING)
        registerUserViewModelTest.getUser().observeForever {
            actual = it.peekContent()
        }

        // act
        registerUserViewModelTest.registerUser(
            User(
                name = "daivid",
                email = "daivid@gmail.com",
                phone = "99 123456789",
                password = "123456",
                state = State("hash_code", "PE", "Pernambuco"),
                city = "Recife"
            )
        )

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenLoadCities shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<ArrayList<String>>(
            status = ViewStateModel.Status.ERROR,
            errors = responseObject
        )
        var actual = ViewStateModel<ArrayList<String>>(status = ViewStateModel.Status.LOADING)
        registerUserViewModelTest.getCities().observeForever {
            actual = it
        }
        val stateId = "5a107a691592ca19f7b4a860"

        // act
        registerUserViewModelTest.loadCities(stateId)

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenLoadStates shouldReturnErrorViewModelState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<ArrayList<State>>(
            status = ViewStateModel.Status.ERROR,
            errors = responseObject
        )
        var actual = ViewStateModel<ArrayList<State>>(status = ViewStateModel.Status.LOADING)
        registerUserViewModelTest.getStates().observeForever {
            actual = it
        }

        // act
        registerUserViewModelTest.loadStates()

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenRegisterUser shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<Session>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        var actual = ViewStateModel<Session>(status = ViewStateModel.Status.LOADING)
        registerUserViewModelTest.getUser().observeForever {
            actual = it.peekContent()
        }

        // act
        registerUserViewModelTest.registerUser(
            User(
                name = "daivid",
                email = "daivid@gmail.com",
                phone = "99 123456789",
                password = "123456",
                state = State("hash_code", "PE", "Pernambuco"),
                city = "Recife"
            )
        )

        // assert
        assertEquals(expected, actual)
    }
}