package br.com.concrete.tentacle.features.registerGame.registerMedia

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.ViewStateModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
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
    fun `givenSuccessfulResponse whenRegisterMedia shouldReturnSuccessState`() {
        // arrange
        val responseJson = getJson("mockjson/registerMedia/register_media_success.json")
        mockResponse200(responseJson)
        val classType = object : TypeToken<BaseModel<Media>>() {}.type
        val baseResponse: BaseModel<Media> = GsonBuilder()
            .create()
            .fromJson(responseJson, classType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = baseResponse.data
        )
        registerMediaViewModel.viewStatusModel.observeForever {
            actual = it.peekContent()
        }

        // act
        registerMediaViewModel.registerMedia(
            "PS3",
            requestGame.data
        )

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenRegisterMedia shouldReturnErrorState`() {
        // arrange
        val reponseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(
                reponseJson,
                ErrorResponse::class.java
            )
        val expected = ViewStateModel(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        registerMediaViewModel.viewStatusModel.observeForever {
            actual = it.peekContent()
        }

        // act
        registerMediaViewModel.registerMedia(
            "PS3",
            requestGame.data
        )

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessResponse whenGetDetailsGame shouldReturnSuccessState`() {
        // arrange
        val responseJson = getJson("mockjson/registerMedia/detail_game_success.json")
        mockResponse200(responseJson)
        val classType = object : TypeToken<BaseModel<Game>>() {}.type
        val baseResponse: BaseModel<Game> = GsonBuilder()
            .create()
            .fromJson(responseJson, classType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = baseResponse.data
        )
        var actual = ViewStateModel<Game>(status = ViewStateModel.Status.LOADING)
        registerMediaViewModel.getDetailGame().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model,
                status = it.peekContent().status
            )
        }

        // act
        registerMediaViewModel.getDetailsGame("game_id")

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenGetDetailsGame shouldReturnErrorState`() {
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
        var actual = ViewStateModel<Game>(status = ViewStateModel.Status.LOADING)
        registerMediaViewModel.getDetailGame().observeForever {
            actual = ViewStateModel(
                model = null,
                status = it.peekContent().status,
                errors = it.peekContent().errors
            )
        }

        // act
        registerMediaViewModel.getDetailsGame("game_id")

        // assert
        assertEquals(expected, actual)
    }
}