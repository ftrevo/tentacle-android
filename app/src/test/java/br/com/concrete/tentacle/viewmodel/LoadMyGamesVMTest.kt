package br.com.concrete.tentacle.viewmodel

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.loadmygames.LoadMyGamesViewModel
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
            actual = it
        }

        loadMyGamesViewModel.loadMyGames()
        assertEquals(expected, actual)

        val result = mockServer.takeRequest()
        val path = "/media?mineOnly=true"
        assertEquals(path, result.path)
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

        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        loadMyGamesViewModel.getMyGames().observeForever {
            actual = it
        }

        loadMyGamesViewModel.loadMyGames()
        assertEquals(expected, actual)

        val result = mockServer.takeRequest()
        val path = "/media?mineOnly=true"
        assertEquals(path, result.path)
    }
}