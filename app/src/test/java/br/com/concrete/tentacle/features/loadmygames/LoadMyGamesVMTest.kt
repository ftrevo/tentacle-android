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
import org.junit.jupiter.api.BeforeEach
import org.koin.standalone.inject

class LoadMyGamesVMTest : BaseViewModelTest() {

    private val loadMyGamesViewModel: LoadMyGamesViewModel by inject()

    @Test
    fun `givenSuccessfulResponse whenLoadMyGames shouldReturnMediaList`() {
        // arrange
        val responseJson = getJson("mockjson/loadmygames/load_my_games_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<MediaResponse>>() {}.type
        val responseObject: BaseModel<MediaResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        var actual: ViewStateModel<ArrayList<Media>> = ViewStateModel(status = ViewStateModel.Status.LOADING)
        loadMyGamesViewModel.getMyGames().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status
            )
        }
        // act
        loadMyGamesViewModel.loadMyGames()

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenError404Response whenLoadMyGames shouldReturnAnError404`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_404.json")
        mockResponseError404()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        var actual: ViewStateModel<ArrayList<Media>> = ViewStateModel(status = ViewStateModel.Status.LOADING)
        loadMyGamesViewModel.getMyGames().observeForever {
            actual = ViewStateModel(
                status = it.peekContent().status,
                model = null,
                errors = it.peekContent().errors
            )
        }
        val expected = ViewStateModel<ArrayList<Media>>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )

        // act
        loadMyGamesViewModel.loadMyGames()

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessfulResponse whenLoadMoreGames shouldReturnMediaList`() {
        // arrange
        val responseJson = getJson("mockjson/loadmygames/load_my_games_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<MediaResponse>>() {}.type
        val responseObject: BaseModel<MediaResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        var actual: ViewStateModel<ArrayList<Media>> = ViewStateModel(status = ViewStateModel.Status.LOADING)
        loadMyGamesViewModel.getMyGamesPage().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status
            )
        }
        loadMyGamesViewModel.queryParameters = null

        // act
        loadMyGamesViewModel.loadGamePage()

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when loadMyGamesViewModel calls loadMoreGames should return an error404`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_404.json")
        mockResponseError404()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<ArrayList<Media>>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        var actual: ViewStateModel<ArrayList<Media>> = ViewStateModel(status = ViewStateModel.Status.LOADING)
        loadMyGamesViewModel.getMyGamesPage().observeForever {
            actual = ViewStateModel(
                status = it.peekContent().status,
                model = null,
                errors = it.peekContent().errors
            )
        }
        loadMyGamesViewModel.queryParameters = null

        // act
        loadMyGamesViewModel.loadGamePage()

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when loadMyGamesViewModel calls loadMyGames with filters should assemble right path`() {
        // arrange
        val expectedPath = "/media-loan?limit=15&page=0&active=false"
        val mockResponse = MockResponse()
            .setResponseCode(200)
        mockServer.enqueue(mockResponse)
        loadMyGamesViewModel.queryParameters = QueryParameters(active = false)

        // act
        loadMyGamesViewModel.loadMyGames()

        // assert
        assertEquals(expectedPath, mockServer.takeRequest().path)
    }

    @Test
    fun `when loadMyGamesViewModel calls loadMoreGames with filters should assemble right path`() {
        // arrange
        val responseJson = getJson("mockjson/loadmygames/load_my_games_success.json")
        mockResponse200(responseJson)
        mockResponse200(responseJson)
        mockResponse200(responseJson)
        mockResponse200(responseJson)
        loadMyGamesViewModel.queryParameters = QueryParameters(active = false)
        loadMyGamesViewModel.loadGamePage()
        loadMyGamesViewModel.loadGamePage()
        loadMyGamesViewModel.loadGamePage()
        loadMyGamesViewModel.loadGamePage()
        mockServer.takeRequest()
        mockServer.takeRequest()
        mockServer.takeRequest()
        val expectedPath = "/media-loan?limit=15&page=4&active=false"

        // act
        val requestedPath = mockServer.takeRequest().path

        // assert
        assertEquals(expectedPath, requestedPath)
    }

    @Test
    fun `when loadMyGamesViewModel calls loadGames with filters active false`() {
        // arrange
        val responseJson = getJson("mockjson/loadmygames/load_my_games_disabled_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<MediaResponse>>() {}.type
        val responseObject: BaseModel<MediaResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        var actual: ViewStateModel<ArrayList<Media>> = ViewStateModel(status = ViewStateModel.Status.LOADING)
        loadMyGamesViewModel.getMyGamesPage().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status
            )
        }
        loadMyGamesViewModel.queryParameters = QueryParameters(active = false)

        // act
        loadMyGamesViewModel.loadGamePage()

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when loadMyGamesViewModel calls deleteMedia`() {
        // arrange
        val responseJson = getJson("mockjson/loadmygames/register_media_deleted_success.json")
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
        loadMyGamesViewModel.deleteMedia().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model,
                status = it.peekContent().status)
        }

        // act
        loadMyGamesViewModel.deleteGame("id")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when loadMyGamesViewModel calls deleteMedia error 404`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        var actual = ViewStateModel<Media>(status = ViewStateModel.Status.LOADING)
        loadMyGamesViewModel.deleteMedia().observeForever {
            actual = ViewStateModel(
                status = it.peekContent().status,
                model = null,
                errors = it.peekContent().errors
            )
        }

        // act
        loadMyGamesViewModel.deleteGame("id")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when loadMyGamesViewModel calls activeMedia`() {
        // arrange
        val responseJson = getJson("mockjson/loadmygames/register_media_deleted_success.json")
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
        loadMyGamesViewModel.activeMediaState().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model,
                status = it.peekContent().status
            )
        }
        val media = Media.getEmptyMedia()

        // act
        loadMyGamesViewModel.activeMedia(media, false)

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when loadMyGamesViewModel calls activeMedia error 404`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.ERROR,
            model = null, errors = responseObject
        )
        var actual = ViewStateModel<Media>(status = ViewStateModel.Status.LOADING)
        loadMyGamesViewModel.activeMediaState().observeForever {
            actual = ViewStateModel(
                status = it.peekContent().status,
                model = null,
                errors = it.peekContent().errors
            )
        }
        val media = Media.getEmptyMedia()

        // act
        loadMyGamesViewModel.activeMedia(media, false)

        // assert
        assertEquals(expected, actual)
    }
}