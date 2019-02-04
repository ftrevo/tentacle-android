package br.com.concrete.tentacle.features.registerGame.registerMedia

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.RegisteredMediaResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.koin.standalone.inject

class RegisterMediaViewModelTest : BaseViewModelTest() {

    private val regMediaviewModel: RegisterMediaViewModel by inject()
    private lateinit var actual: ViewStateModel<RegisteredMediaResponse>

    private lateinit var requestGame: Game

    @Before
    fun setup() {
        val gameJson = getJson("mockjson/registerMedia/game.json")
        requestGame = GsonBuilder()
            .create()
            .fromJson(gameJson, Game::class.java)
    }

    @Test
    fun `when viewmodel successfully register a game should change status to SUCCESS`() {
        val responseJson = getJson("mockjson/registerMedia/register_media_success.json")

        val klass = object : TypeToken<BaseModel<RegisteredMediaResponse>>() {}.type
        val baseResponse: BaseModel<RegisteredMediaResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, klass)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)
        mockServer.enqueue(mockResponse)

        regMediaviewModel.viewStatusModel.observeForever {
            actual = it
        }

        regMediaviewModel.registerMedia(
            "PS3",
            requestGame
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
            GsonBuilder().create().fromJson(errorResponseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)

        regMediaviewModel.viewStatusModel.observeForever {
            actual = it
        }

        regMediaviewModel.registerMedia(
            "PS3",
            requestGame
        )
        assertEquals(expected, actual)
    }
}