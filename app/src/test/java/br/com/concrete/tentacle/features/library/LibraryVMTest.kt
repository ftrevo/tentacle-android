package br.com.concrete.tentacle.features.library

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.models.filter.SubItem
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
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status)
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
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status, errors = it.peekContent().errors)
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
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status, errors = it.peekContent().errors)
        }
        libraryViewModel.loadLibrary()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when libraryViewModel calls loadlibrary should return a list of Library after filter`() {
        val filtersSelected = ArrayList<SubItem>()
        filtersSelected.add(SubItem("Plataformas", "PS4", "Playstation 4", true))

        val queryParameters = QueryUtils.assemblefilterQuery(filtersSelected)

        val responseJson = getJson(
            "mockjson/library/get_library_after_filter_success.json"
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
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status)
        }

        libraryViewModel.loadLibrary(queryParameters)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when libraryViewModel calls loadlibrary should return an empty list of Library after filter`() {
        val filtersSelected = ArrayList<SubItem>()
        filtersSelected.add(SubItem("Plataformas", "PS4", "Playstation 4", true))

        val queryParameters = QueryUtils.assemblefilterQuery(filtersSelected)

        val responseJson = getJson(
            "mockjson/common/common_empty_list_success.json"
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
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status)
        }

        libraryViewModel.loadLibrary(queryParameters)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when libraryViewModel calls loadlibrary should return a list of Library after searchview`() {
        val queryParameters = QueryParameters()

        val responseJson = getJson(
            "mockjson/library/get_library_search_game_fifa_success.json"
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
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status)
        }

        libraryViewModel.loadLibrary(queryParameters, "fifa")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when libraryViewModel calls loadlibrary should return a list of Library after searchview with filter fill`() {
        val filtersSelected = ArrayList<SubItem>()
        filtersSelected.add(SubItem("Plataformas", "PS4", "Playstation 4", true))

        val queryParameters = QueryUtils.assemblefilterQuery(filtersSelected)

        val responseJson = getJson(
            "mockjson/library/get_library_search_game_fifa_success.json"
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
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status)
        }

        libraryViewModel.loadLibrary(queryParameters, "fifa")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when libraryViewModel calls loadlibrary should return an error message for 400 after searchview`() {
        val queryParameters = QueryParameters()

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
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status, errors = it.peekContent().errors)
        }

        libraryViewModel.loadLibrary(queryParameters, "fifa")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when libraryViewModel calls loadlibrary should return an error message for 400 after searchview with filter fill`() {
        val filtersSelected = ArrayList<SubItem>()
        filtersSelected.add(SubItem("Plataformas", "PS4", "Playstation 4", true))

        val queryParameters = QueryUtils.assemblefilterQuery(filtersSelected)

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
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status, errors = it.peekContent().errors)
        }

        libraryViewModel.loadLibrary(queryParameters, "fifa")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when libraryViewModel calls loadlibrary should return a list of Library endLessRecyclerView`() {
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
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status)
        }

        mockServer.enqueue(mockResponse)

        libraryViewModel.getLibrary().observeForever {
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status)
        }

        libraryViewModel.loadLibraryMore()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when libraryViewModel calls loadlibrary should return a list of Library endLessRecyclerView with error 400 after scroll`() {
        val responseJsonError = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObjectError: ErrorResponse =
            GsonBuilder().create().fromJson(responseJsonError, ErrorResponse::class.java)

        val expectedError =
            ViewStateModel<ArrayList<Library>>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObjectError)
        var actualError = ViewStateModel<ArrayList<Library>>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        libraryViewModel.getLibraryMore().observeForever {
            actualError = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status, errors = it.peekContent().errors)
        }

        libraryViewModel.loadLibraryMore()
        Assert.assertEquals(expectedError, actualError)
    }

    @Test
    fun `when LibraryViewModel calls loadLibrary should of Library endLessRecyclerView return error message for 401`() {
        val expected =
            ViewStateModel<ArrayList<Library>>(
                status = ViewStateModel.Status.ERROR, model = null, errors = ErrorResponse()
            )
        var actual = ViewStateModel<ArrayList<Library>>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(401)

        mockServer.enqueue(mockResponse)

        libraryViewModel.getLibraryMore().observeForever {
            actual = ViewStateModel(model = it.peekContent().model?.list, status = it.peekContent().status, errors = it.peekContent().errors)
        }
        libraryViewModel.loadLibraryMore()
        Assert.assertEquals(expected, actual)
    }
}