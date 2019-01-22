package br.com.concrete.tentacle.viewmodel

import android.util.Log
import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.searchGame.SearchGameViewModel
import br.com.concrete.tentacle.mock.MockGameJson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.standalone.inject

class SearchViewModelTest: BaseViewModelTest() {

    private val searchGameViewModel: SearchGameViewModel by inject()

    @Test
    fun `when searchGameViewModel calls searchGame should return a list of Games`() {

        val collectionType = object : TypeToken<BaseModel<GameResponse>>(){}.type
        val responseObject: BaseModel<GameResponse> = GsonBuilder()
            .create()
            .fromJson(MockGameJson.LIST_GAME_SUCCESS, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list)
        var actual = ViewStateModel<ArrayList<Game>>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(MockGameJson.LIST_GAME_SUCCESS)
        mockServer.enqueue(mockResponse)

        searchGameViewModel.getSearchGame().observeForever {
            actual = it
        }

        searchGameViewModel.searchGame("Fifa")
        assertEquals(expected, actual)
        val result = mockServer.takeRequest()
        Log.i("PATH ", result.path)


    }

}