package br.com.concrete.tentacle.mock

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.StateResponse
import br.com.concrete.tentacle.data.models.CityResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

private val messageSuccess = listOf("success")

/**
 * COMMON
 */
val errorResponse = ErrorResponse()

var error400 = Throwable(HttpException(
    Response.error<HttpException>(400,
        ResponseBody.create(MediaType.parse("application/json"),
            "{\"message\": [\"Error Message 01\", \"Error Message 02\"]}"
        )
    ))
)

val error401 = Throwable(
    HttpException(
        Response.error<HttpException>(401,
            ResponseBody.create(MediaType.parse("text/plain"), "")
        ))
)

val baseModelStateSuccess = BaseModel(messageSuccess, StateResponse(getStates()))
fun getStates(): ArrayList<State> {
    val states = ArrayList<State>()

    states.add(State(
        _id = "5a107a691592ca19f7b4a86b",
        initials = "TO",
        name = "Tocantins"
    ))
    states.add(State(
        _id = "5a107a691592ca19f7b4a86a",
        initials = "SP",
        name = "São Paulo"
    ))

    return states
}

/**
 * MOCK USED FOR CITIES
 */
fun getCities(): ArrayList<String> {
    val cities = ArrayList<String>()
    cities.add("São Paulo")
    cities.add("Santos")
    cities.add("São Vicente")
    return cities
}
const val requestedState = "5a107a691592ca19f7b4a86a"
val baseModelCitiesSuccess = BaseModel(messageSuccess, CityResponse(getCities(), requestedState))

/**
 * MOCK USED FOR USER
 */
private val user = User(
    _id = "1",
    name = "daivid",
    email = "daivid@gmail.com",
    phone = "99 123456789",
    password = "123456",
    state = State("hash_code", "PE", "Pernambuco"),
    city = "Recife",
    createdAt = "today",
    updatedAt = "today"
)
val userRequest = UserRequest(
    name = "daivid",
    email = "daivid@gmail.com",
    phone = "99 123456789",
    password = "123456",
    state = "hash_code",
    city = "Recife"
)
val baseModelUserSuccess = BaseModel(messageSuccess, user)

/**
 * this block is to mock the apiService result
 * in order to verify and make a unit test for the repository
 */
val session = Session(accessToken = "ACCESS_TOKEN",
    refreshToken = "REFRESH_TOKEN",
    tokenType = "TOKEN_TYPE")
val message = listOf(
    "LOGGIN SUCCESS"
)
val baseModelLoginSuccess = BaseModel(message, session)

/**
 * Mock for shared preferences
 */
const val stringKey = "KEY"
const val string = "STRING"

const val sessionKey = "sessionKey"
val sessionForPreference = session
const val stringExpectedWhenThereIsNoOne = ""