package br.com.concrete.tentacle.features.registerGame.registerMedia

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.ViewStateModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.koin.standalone.inject

class RegisterMediaViewModelTest : BaseViewModelTest() {

    private val registerMediaViewModel: RegisterMediaViewModel by inject()
    private lateinit var actual: ViewStateModel<Media>

    private lateinit var requestGame: BaseModel<Game>

    @Before
    fun setup() {
        val gameJson = getJson("mockjson/registerMedia/detail_game_success.json")
        val classType = object : TypeToken<BaseModel<Game>>() {}.type
        requestGame = GsonBuilder()
        .create()
            .fromJson(gameJson, classType)
    }

    @Test
    fun `when viewmodel successfully register a game should change status to SUCCESS`() {
        val responseJson = getJson("mockjson/registerMedia/register_media_success.json")

        val classType = object : TypeToken<BaseModel<Media>>() {}.type
        val baseResponse: BaseModel<Media> = GsonBuilder()
            .create()
            .fromJson(responseJson, classType)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)
        mockServer.enqueue(mockResponse)

        registerMediaViewModel.viewStatusModel.observeForever {
            actual = it.peekContent()
        }

        registerMediaViewModel.registerMedia(
            "PS3",
            requestGame.data
        )

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = baseResponse.data)

        assertEquals(expected, actual)
    }

    @Test
    fun `when something goes wrong trying to register should change status to ERROR`() {
        val errorResponseJson = getJson(
            "mockjson/errors/error_400.json"
        )
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody(errorResponseJson)
        mockServer.enqueue(mockResponse)

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(
                errorResponseJson,
                ErrorResponse::class.java
            )

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.ERROR,
                model = null,
                errors = responseObject)

        registerMediaViewModel.viewStatusModel.observeForever {
            actual = it.peekContent()
        }

        registerMediaViewModel.registerMedia(
            "PS3",
            requestGame.data
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `when viewmodel successfully detail a game should change status to SUCCESS`() {
        val responseJson = getJson("mockjson/registerMedia/detail_game_success.json")
        var actualDetail = ViewStateModel<Game>(status = ViewStateModel.Status.LOADING)

        val classType = object : TypeToken<BaseModel<Game>>() {}.type
        val baseResponse: BaseModel<Game> = GsonBuilder()
            .create()
            .fromJson(responseJson, classType)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)
        mockServer.enqueue(mockResponse)

        registerMediaViewModel.getDetailGame().observeForever {
            actualDetail = ViewStateModel(model = it.peekContent().model, status = it.peekContent().status)
        }

        registerMediaViewModel.getDetailsGame("game_id")

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = baseResponse.data)

        assertEquals(expected, actualDetail)
    }

    @Test
    fun `when viewmodel successfully detail a game should change status to ERROR`() {
        var actualDetail = ViewStateModel<Game>(status = ViewStateModel.Status.LOADING)

        val errorResponseJson = getJson(
            "mockjson/errors/error_400.json"
        )
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody(errorResponseJson)
        mockServer.enqueue(mockResponse)

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(
                errorResponseJson,
                ErrorResponse::class.java
            )

        registerMediaViewModel.getDetailGame().observeForever {
            actualDetail = ViewStateModel(
                model = null,
                status = it.peekContent().status,
                errors = it.peekContent().errors
            )
        }

        registerMediaViewModel.getDetailsGame("game_id")

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.ERROR,
                model = null,
                errors = responseObject
            )

        assertEquals(expected, actualDetail)
    }
}