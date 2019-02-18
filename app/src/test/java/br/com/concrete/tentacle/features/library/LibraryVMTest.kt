package br.com.concrete.tentacle.features.library

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.models.library.filter.SubItem
import br.com.concrete.tentacle.utils.QueryUtils
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Test
import org.koin.standalone.inject

class LibraryVMTest : BaseViewModelTest() {

    private val libraryViewModel: LibraryViewModel by inject()

    @Test
    fun `when libraryViewModel calls loadlibrary should return a list of Library`() {
        val responseJson = getJson(
            "mockjson/library/get_library_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<LibraryResponse>>() {}.type
        val responseObject: BaseModel<LibraryResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list)
        var actual = ViewStateModel<ArrayList<Library>>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        libraryViewModel.getLibrary().observeForever {
            actual = it
        }

        libraryViewModel.loadLibrary()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when LibraryViewModel calls loadLibrary should return error message for 401`() {
        val expected =
            ViewStateModel<ArrayList<Library>>(
                status = ViewStateModel.Status.ERROR, model = null, errors = ErrorResponse()
            )
        var actual = ViewStateModel<ArrayList<Library>>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(401)

        mockServer.enqueue(mockResponse)

        libraryViewModel.getLibrary().observeForever {
            actual = it
        }
        libraryViewModel.loadLibrary()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when LibraryViewModel calls loadLibrary should return error message for 400`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<ArrayList<Library>>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)
        var actual = ViewStateModel<ArrayList<Library>>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        libraryViewModel.getLibrary().observeForever {
            actual = it
        }
        libraryViewModel.loadLibrary()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when libraryViewModel calls loadlibrary should return a list of Library after filter`() {
        val responseJson = getJson(
            "mockjson/library/get_library_after_filter_success.json"
        )
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        val filtersSelected = ArrayList<SubItem>()
        filtersSelected.add(SubItem("Plataformas", "PS4", "Playstation 4", true))

        val queryParameters = QueryUtils.assemblefilterQuery(filtersSelected)

        libraryViewModel.loadLibrary(queryParameters)

        val collectionType = object : TypeToken<BaseModel<LibraryResponse>>() {}.type
        val responseObject: BaseModel<LibraryResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list)
        var actual = ViewStateModel<ArrayList<Library>>(status = ViewStateModel.Status.LOADING)

        libraryViewModel.getLibrary().observeForever {
            actual = it
        }

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when libraryViewModel calls loadlibrary should return a list of Library after filter without items`() {
        val responseJson = getJson(
            "mockjson/library/get_library_error.json"
        )
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        val filtersSelected = ArrayList<SubItem>()
        filtersSelected.add(SubItem("Plataformas", "PS4", "Playstation 4", true))

        val queryParameters = QueryUtils.assemblefilterQuery(filtersSelected)

        libraryViewModel.loadLibrary(queryParameters)

        val collectionType = object : TypeToken<BaseModel<LibraryResponse>>() {}.type
        val responseObject: BaseModel<LibraryResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data.list)
        var actual = ViewStateModel<ArrayList<Library>>(status = ViewStateModel.Status.LOADING)

        libraryViewModel.getLibrary().observeForever {
            actual = it
        }

        Assert.assertEquals(expected, actual)
    }
}