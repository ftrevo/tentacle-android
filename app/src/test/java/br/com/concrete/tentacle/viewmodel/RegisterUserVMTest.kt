package br.com.concrete.tentacle.viewmodel

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.CityResponse
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.StateResponse
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.register.RegisterUserViewModel
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.standalone.inject

class RegisterUserVMTest : BaseViewModelTest() {

    private val registerUserViewModelTest: RegisterUserViewModel by inject()

    @Test
    fun `when registerUserViewModel calls loadCities should return a list of strings`() {
        val responseJson = getJson(
            "mockjson/user/get_cities_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<CityResponse>>() {}.type
        val responseObject: BaseModel<CityResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.cities)
            var actual = ViewStateModel<ArrayList<String>>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        registerUserViewModelTest.getCities().observeForever {
            actual = it
        }

        val stateId = "5a107a691592ca19f7b4a860"
        registerUserViewModelTest.loadCities(stateId)
        assertEquals(expected, actual)
        val result = mockServer.takeRequest()
        val path = "/states/" + stateId + "/cities"
        assertEquals(path, result.path)
    }

    @Test
    fun `when registerUserViewModel calls loadStates should return a list of states`() {
        val responseJson = getJson(
            "mockjson/user/get_states_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<StateResponse>>() {}.type
        val responseObject: BaseModel<StateResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list)
        var actual = ViewStateModel<ArrayList<State>>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        registerUserViewModelTest.getStates().observeForever {
            actual = it
        }

        registerUserViewModelTest.loadStates()
        assertEquals(expected, actual)
        val result = mockServer.takeRequest()
        assertEquals("/states", result.path)
    }

    @Test
    fun `when registerUserViewModel calls registerUser should return a userRegistered`() {
        val responseJson = getJson(
            "mockjson/user/register_user_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<User>>() {}.type
        val responseObject: BaseModel<User> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data)

        var actual = ViewStateModel<User>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        registerUserViewModelTest.getUser().observeForever {
            actual = it
        }

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
        assertEquals(expected, actual)
        val result = mockServer.takeRequest()
        assertEquals("/users", result.path)
    }

    @Test
    fun `when registerUserViewModel calls loadCities should error message`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<ArrayList<String>>(
                status = ViewStateModel.Status.ERROR,
                errors = responseObject)
        var actual = ViewStateModel<ArrayList<String>>(status = ViewStateModel.Status.LOADING)

        mockResponseError400(responseJson)

        registerUserViewModelTest.getCities().observeForever {
            actual = it
        }

        val stateId = "5a107a691592ca19f7b4a860"
        registerUserViewModelTest.loadCities(stateId)
        assertEquals(expected, actual)
        val result = mockServer.takeRequest()
        val path = "/states/" + stateId + "/cities"
        assertEquals(path, result.path)
    }

    @Test
    fun `when registerUserViewModel calls loadStates should return error message`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<ArrayList<State>>(
                status = ViewStateModel.Status.ERROR,
                errors = responseObject)
        var actual = ViewStateModel<ArrayList<State>>(status = ViewStateModel.Status.LOADING)

        mockResponseError400(responseJson)

        registerUserViewModelTest.getStates().observeForever {
            actual = it
        }

        registerUserViewModelTest.loadStates()
        assertEquals(expected, actual)
        val result = mockServer.takeRequest()
        assertEquals("/states", result.path)
    }

    @Test
    fun `when registerUserViewModel calls registerUser should return error message`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<User>(
                status = ViewStateModel.Status.ERROR,
                errors = responseObject)

        var actual = ViewStateModel<User>(status = ViewStateModel.Status.LOADING)

        mockResponseError400(responseJson)

        registerUserViewModelTest.getUser().observeForever {
            actual = it
        }

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
        assertEquals(expected, actual)
        val result = mockServer.takeRequest()
        assertEquals("/users", result.path)
    }
}