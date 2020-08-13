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
    fun `when searchGameViewModel calls searchGame should return a list of Games`() {
        val responseJson = getJson(
            "mockjson/searchgame/list_game_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<GameResponse>>() {}.type
        val responseObject: BaseModel<GameResponse> = GsonBuilder().create().fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list
            )
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)

        mockResponse200(responseJson)

        searchGameViewModel.getSearchGame().observeForever {
            actual = ViewStateModel(model = it.model?.list, status = ViewStateModel.Status.SUCCESS)
        }

        searchGameViewModel.searchGame("Fifa")
        assertEquals(expected, actual)
    }

    @Test
    fun `when searchGameViewModel calls searchGame should return a list of Games empty`() {
        val responseJson = getJson(
            "mockjson/searchgame/list_game_empty_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<GameResponse>>() {}.type
        val responseObject: BaseModel<GameResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list
            )
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)

        mockResponse200(responseJson)

        searchGameViewModel.getSearchGame().observeForever {
            actual = ViewStateModel(model = it.model?.list, status = ViewStateModel.Status.SUCCESS)
        }

        searchGameViewModel.searchGame("Fifa")
        assertEquals(expected, actual)
    }

    @Test
    fun `when searchGameViewModel calls searchGame should return a list of Games error404`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<ArrayList<Game>>(
                status = ViewStateModel.Status.ERROR, model = null
            )
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        searchGameViewModel.getSearchGame().observeForever {
            actual = ViewStateModel(model = it.model?.list, status = it.status)
        }

        searchGameViewModel.searchGame("Fifa")
        assertEquals(expected, actual)
    }

    @Test
    fun `when searchGameViewModel calls registerNewGame should return a object Game`() {
        val responseJson = getJson(
            "mockjson/searchgame/register_game_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<Game>>() {}.type
        val responseObject: BaseModel<Game> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
                ViewStateModel(
                    status = ViewStateModel.Status.SUCCESS,
                    model = responseObject.data
                )

        var actual =
            ViewStateModel<Game>(
                status = ViewStateModel.Status.LOADING
            )

        mockResponse201(responseJson)

        searchGameViewModel.getRegisteredGame().observeForever {
            actual = it.peekContent()
        }

        searchGameViewModel.registerNewGame("Fallout 76")
        assertEquals(expected, actual)
    }

    @Test
    fun `when searchGameViewModel calls registerNewGame should return a object of Games error404`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<Game>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject
            )

        var actual =
            ViewStateModel<Game>(
                status = ViewStateModel.Status.LOADING
            )

        mockResponseError400()

        searchGameViewModel.getRegisteredGame().observeForever {
            actual = it.peekContent()
        }

        searchGameViewModel.registerNewGame("Fallout 76")
        assertEquals(expected, actual)
    }

    @Test
    fun `when searchGameViewModel calls searchMoreGame should return a list of Games`() {
        val responseJson = getJson(
            "mockjson/searchgame/list_game_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<GameResponse>>() {}.type
        val responseObject: BaseModel<GameResponse> = GsonBuilder().create().fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list
            )
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)

        mockResponse200(responseJson)

        searchGameViewModel.getSearchMoreGame().observeForever {
            actual = ViewStateModel(model = it.peekContent().model?.list, status = ViewStateModel.Status.SUCCESS)
        }

        searchGameViewModel.searchGameMore("Fifa")
        assertEquals(expected, actual)
    }
}