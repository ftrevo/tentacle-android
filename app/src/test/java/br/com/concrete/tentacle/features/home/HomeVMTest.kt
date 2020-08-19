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

class HomeVMTest : BaseViewModelTest() {

    val homeViewModel by inject<HomeViewModel>()

    @Test
    fun `when HomeViewModel calls getHomeGames should return error message for 400`() {

        // arrange
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )
        mockResponseError400()
        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<ArrayList<Game>>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)

        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)
        homeViewModel.getHomeGames().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model as? ArrayList<Game>?,
                status = it.peekContent().status,
                errors = it.peekContent().errors)
        }

        // act
        homeViewModel.loadHomeGames()

        //assert
        assertEquals(expected.status, actual.status)
    }

    @Test
    fun `when HomeViewModel calls getHomeGames should return success`() {

        // arrange
        val responseJson = getJson(
            "mockjson/home/new_home_games_success.json"
        )
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<GameResponse>>() {}.type
        val responseObject: BaseModel<GameResponse> =
            GsonBuilder().create().fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list)

        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)
        homeViewModel.getHomeGames().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model as ArrayList<Game>?,
                status = it.peekContent().status)
        }

        // act
        homeViewModel.loadHomeGames()

        // assert
        assertEquals(expected, actual)
    }
}