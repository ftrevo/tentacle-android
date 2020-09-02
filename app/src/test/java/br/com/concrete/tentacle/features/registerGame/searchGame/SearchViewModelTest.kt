package br.com.concrete.tentacle.features.registerGame.searchGame

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.standalone.inject

class SearchViewModelTest : BaseViewModelTest() {

    private val searchGameViewModel: SearchGameViewModel by inject()

    @Test
    fun `givenSuccessfulResponse whenSearchGame shouldReturnListOfGames`() {
        // arrange
        val responseJson = getJson("mockjson/searchgame/list_game_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<GameResponse>>() {}.type
        val responseObject: BaseModel<GameResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)
        searchGameViewModel.getSearchGame().observeForever {
            actual = ViewStateModel(model = it.model?.list, status = ViewStateModel.Status.SUCCESS)
        }

        // act
        searchGameViewModel.searchGame("Fifa")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessfulResponse whenSearchGame shouldReturnEmptyList`() {
        // arrange
        val responseJson = getJson("mockjson/searchgame/list_game_empty_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<GameResponse>>() {}.type
        val responseObject: BaseModel<GameResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)
        searchGameViewModel.getSearchGame().observeForever {
            actual = ViewStateModel(model = it.model?.list, status = ViewStateModel.Status.SUCCESS)
        }

        // act
        searchGameViewModel.searchGame("Fifa")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenSearchGame shouldReturnErroState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<ArrayList<Game>>(
            status = ViewStateModel.Status.ERROR,
            model = null
        )
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)
        searchGameViewModel.getSearchGame().observeForever {
            actual = ViewStateModel(model = it.model?.list, status = it.status)
        }

        // act
        searchGameViewModel.searchGame("Fifa")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessfulResponse whenRegisterNewGame shouldReturnSuccessState`() {
        // arrange
        val responseJson = getJson("mockjson/searchgame/register_game_success.json")
        mockResponse201(responseJson)
        val collectionType = object : TypeToken<BaseModel<Game>>() {}.type
        val responseObject: BaseModel<Game> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data
        )
        var actual = ViewStateModel<Game>(status = ViewStateModel.Status.LOADING)
        searchGameViewModel.getRegisteredGame().observeForever {
            actual = it.peekContent()
        }

        // act
        searchGameViewModel.registerNewGame("Fallout 76")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenRegisterNewGame shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<Game>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        var actual = ViewStateModel<Game>(
            status = ViewStateModel.Status.LOADING
        )
        searchGameViewModel.getRegisteredGame().observeForever {
            actual = it.peekContent()
        }

        // act
        searchGameViewModel.registerNewGame("Fallout 76")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessfulResponse whenSearchMoreGame shouldReturnListOfGames`() {
        // arrange
        val responseJson = getJson("mockjson/searchgame/list_game_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<GameResponse>>() {}.type
        val responseObject: BaseModel<GameResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)
        searchGameViewModel.getSearchMoreGame().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = ViewStateModel.Status.SUCCESS
            )
        }

        // act
        searchGameViewModel.searchGameMore("Fifa")

        // assert
        assertEquals(expected, actual)
    }
}