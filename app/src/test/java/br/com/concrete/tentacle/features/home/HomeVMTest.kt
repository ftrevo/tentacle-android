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
import org.junit.jupiter.api.BeforeEach
import org.koin.standalone.inject

class HomeVMTest : BaseViewModelTest() {

    val homeViewModel by inject<HomeViewModel>()
    lateinit var actual: ViewStateModel<ArrayList<Game>>

    @BeforeEach
    fun initializeValidationVariable(){
        actual = ViewStateModel(status = ViewStateModel.Status.LOADING)
    }

    @Test
    fun `givenErrorResponse whenGetHomeGames shouldReturnErrorStatus`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_404.json")
        mockResponseError404()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<ArrayList<Game>>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        homeViewModel.getHomeGames().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model as? ArrayList<Game>?,
                status = it.peekContent().status,
                errors = it.peekContent().errors
            )
        }

        // act
        homeViewModel.loadHomeGames()

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessfulResponse whenGetHomeGames shouldReturnSuccessStatus`() {
        // arrange
        val responseJson = getJson("mockjson/home/new_home_games_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<GameResponse>>() {}.type
        val responseObject: BaseModel<GameResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        homeViewModel.getHomeGames().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model as ArrayList<Game>?,
                status = it.peekContent().status
            )
        }

        // act
        homeViewModel.loadHomeGames()

        // assert
        assertEquals(expected, actual)
    }
}