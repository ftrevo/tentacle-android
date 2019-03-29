package br.com.concrete.tentacle.features.loadmygames

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.standalone.inject

class LoadMyGamesVMTest : BaseViewModelTest() {

    private val loadMyGamesViewModel: LoadMyGamesViewModel by inject()

    @Test
    fun `when loadMyGamesViewModel calls loadMyGames should return a list of medias`() {
        val responseJson = getJson(
            "mockjson/loadmygames/load_my_games_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<MediaResponse>>() {}.type
        val responseObject: BaseModel<MediaResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list)
        var actual = ViewStateModel<ArrayList<Media>>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        loadMyGamesViewModel.getMyGames().observeForever {
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status)
        }

        loadMyGamesViewModel.loadMyGames()
        assertEquals(expected, actual)
    }

    @Test
    fun `when loadMyGamesViewModel calls loadMyGames should return an error404`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<ArrayList<Media>>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)
        var actual = ViewStateModel<ArrayList<Media>>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        loadMyGamesViewModel.getMyGames().observeForever {
            actual =
                ViewStateModel(status = it.peekContent().status, model = null, errors = it.peekContent().errors)
        }

        loadMyGamesViewModel.loadMyGames()
        assertEquals(expected, actual)
    }

    @Test
    fun `when loadMyGamesViewModel calls loadMoreGames should return a list of medias`() {
        val responseJson = getJson(
            "mockjson/loadmygames/load_my_games_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<MediaResponse>>() {}.type
        val responseObject: BaseModel<MediaResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list)
        var actual = ViewStateModel<ArrayList<Media>>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        loadMyGamesViewModel.getMyGamesPage().observeForever {
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status)
        }

        loadMyGamesViewModel.queryParameters = null
        loadMyGamesViewModel.loadGamePage()
        assertEquals(expected, actual)
    }

    @Test
    fun `when loadMyGamesViewModel calls loadMoreGames should return an error404`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<ArrayList<Media>>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)
        var actual = ViewStateModel<ArrayList<Media>>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        loadMyGamesViewModel.getMyGamesPage().observeForever {
            actual =
                ViewStateModel(status = it.peekContent().status, model = null, errors = it.peekContent().errors)
        }

        loadMyGamesViewModel.queryParameters = null
        loadMyGamesViewModel.loadGamePage()
        assertEquals(expected, actual)
    }

    @Test
    fun `when loadMyGamesViewModel calls loadMyGames with filters should assemble right path`() {
        val expectedPath = "/media-loan?limit=15&page=0&active=false"

        val mockResponse = MockResponse()
            .setResponseCode(200)
        mockServer.enqueue(mockResponse)

        loadMyGamesViewModel.queryParameters = QueryParameters(active = false)
        loadMyGamesViewModel.loadMyGames()

        val requestedPath = mockServer.takeRequest().path
        assertEquals(expectedPath, requestedPath)
    }

    @Test
    fun `when loadMyGamesViewModel calls loadMoreGames with filters should assemble right path`() {
        val responseJson = getJson(
            "mockjson/loadmygames/load_my_games_success.json"
        )
        val expectedPath = "/media-loan?limit=15&page=4&active=false"

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)
        mockServer.enqueue(mockResponse)
        mockServer.enqueue(mockResponse)
        mockServer.enqueue(mockResponse)

        loadMyGamesViewModel.queryParameters = QueryParameters(active = false)
        loadMyGamesViewModel.loadGamePage()
        loadMyGamesViewModel.loadGamePage()
        loadMyGamesViewModel.loadGamePage()
        loadMyGamesViewModel.loadGamePage()

        mockServer.takeRequest()
        mockServer.takeRequest()
        mockServer.takeRequest()
        val requestedPath = mockServer.takeRequest().path
        assertEquals(expectedPath, requestedPath)
    }
}