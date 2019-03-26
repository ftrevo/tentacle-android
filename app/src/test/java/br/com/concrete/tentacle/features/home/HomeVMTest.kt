package br.com.concrete.tentacle.features.home

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.standalone.inject

class HomeVMTest : BaseViewModelTest()    {

    val homeViewModel by inject<HomeViewModel>()


    @Test
    fun `when HomeViewModel calls getHomeGames should return error message for 401`() {

        val expected =
            ViewStateModel<ArrayList<Game>>(
                status = ViewStateModel.Status.ERROR, model = null, errors = ErrorResponse(statusCode = 401)
            )
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(401)

        mockServer.enqueue(mockResponse)

        homeViewModel.getHomeGames().observeForever {
            actual = ViewStateModel(model = it.model as ArrayList<Game>?, status = it.status, errors = it.errors)
        }
        homeViewModel.loadHomeGames()
        assertEquals(expected, actual)
    }

    @Test
    fun `when HomeViewModel calls getHomeGames should return error message for 400`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<ArrayList<Game>>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        homeViewModel.getHomeGames().observeForever {
            actual = ViewStateModel(model = it.model as ArrayList<Game>?, status = it.status, errors = it.errors)
        }
        homeViewModel.loadHomeGames()
        assertEquals(expected.status, actual.status)
    }

    @Test
    fun `when HomeViewModel calls getHomeGames should return success`() {
        val responseJson = getJson(
            "mockjson/home/new_home_games_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<GameResponse>>() {}.type
        val responseObject: BaseModel<GameResponse> =
            GsonBuilder().create().fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list)
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        homeViewModel.getHomeGames().observeForever {
            actual = ViewStateModel(model = it.model as ArrayList<Game>?, status = it.status)
        }
        homeViewModel.loadHomeGames()
        assertEquals(expected, actual)
    }
}