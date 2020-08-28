package br.com.concrete.tentacle.features.library

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.filter.SubItem
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.utils.QueryUtils
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.koin.standalone.inject

class LibraryVMTest : BaseViewModelTest() {

    private val libraryViewModel: LibraryViewModel by inject()
    lateinit var actual: ViewStateModel<ArrayList<Library>>

    @BeforeEach
    fun initializeValidationVariable() {
        actual = ViewStateModel(status = ViewStateModel.Status.LOADING)
    }

    @Test
    fun `givenSuccessfulResponse whenLoadlibrary shouldReturnLibraryList`() {
        // arrange
        val responseJson = getJson("mockjson/library/get_library_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<LibraryResponse>>() {}.type
        val responseObject: BaseModel<LibraryResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        libraryViewModel.getLibrary().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status
            )
        }

        // act
        libraryViewModel.loadLibrary()

        // assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenLoadLibrary shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<ArrayList<Library>>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        libraryViewModel.getLibrary().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status,
                errors = it.peekContent().errors
            )
        }

        // act
        libraryViewModel.loadLibrary()

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessfulFilteredResponse whenLoadlibrary shouldReturnFilteredLibraryList`() {
        // arrange
        val responseJson = getJson("mockjson/library/get_library_after_filter_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<LibraryResponse>>() {}.type
        val responseObject: BaseModel<LibraryResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        libraryViewModel.getLibrary().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status
            )
        }
        val filtersSelected = ArrayList<SubItem>()
        filtersSelected.add(
            SubItem("Plataformas", "PS4", "Playstation 4", true)
        )
        val queryParameters = QueryUtils.assemblefilterQuery(filtersSelected)

        // act
        libraryViewModel.loadLibrary(queryParameters)

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessfulFilteredResponse whenLoadlibrary shouldReturnEmptyLibraryList`() {
        // arrange
        val responseJson = getJson("mockjson/common/common_empty_list_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<LibraryResponse>>() {}.type
        val responseObject: BaseModel<LibraryResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        libraryViewModel.getLibrary().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status
            )
        }
        val filtersSelected = ArrayList<SubItem>()
        filtersSelected.add(
            SubItem("Plataformas", "PS4", "Playstation 4", true)
        )
        val queryParameters = QueryUtils.assemblefilterQuery(filtersSelected)

        // act
        libraryViewModel.loadLibrary(queryParameters)

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessResponseAfterSearchForAGame whenLoadlibrary shouldReturnLibraryList`() {
        // arrange
        val responseJson = getJson("mockjson/library/get_library_search_game_fifa_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<LibraryResponse>>() {}.type
        val responseObject: BaseModel<LibraryResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        libraryViewModel.getLibrary().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status
            )
        }
        val queryParameters = QueryParameters()

        // act
        libraryViewModel.loadLibrary(queryParameters, "fifa")

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenFilterFilledSuccessfulResponse whenLoadlibrary shouldReturnLibraryList`() {
        // arrange
        val responseJson = getJson("mockjson/library/get_library_search_game_fifa_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<LibraryResponse>>() {}.type
        val responseObject: BaseModel<LibraryResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        libraryViewModel.getLibrary().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status
            )
        }
        val filtersSelected = ArrayList<SubItem>()
        filtersSelected.add(
            SubItem("Plataformas", "PS4", "Playstation 4", true)
        )
        val queryParameters = QueryUtils.assemblefilterQuery(filtersSelected)

        // act
        libraryViewModel.loadLibrary(queryParameters, "fifa")

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenErrorResponse whenLoadlibrary shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val queryParameters = QueryParameters()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<ArrayList<Library>>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        libraryViewModel.getLibrary().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status,
                errors = it.peekContent().errors
            )
        }

        // act
        libraryViewModel.loadLibrary(queryParameters, "fifa")

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenFilterFilledError400Response whenLoadlibrary shouldReturnErrorState`() {
        // arrange
        val responseJson = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObject: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJson, ErrorResponse::class.java)
        val expected = ViewStateModel<ArrayList<Library>>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObject
        )
        libraryViewModel.getLibrary().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status,
                errors = it.peekContent().errors
            )
        }
        val filtersSelected = ArrayList<SubItem>()
        filtersSelected.add(
            SubItem("Plataformas", "PS4", "Playstation 4", true)
        )
        val queryParameters = QueryUtils.assemblefilterQuery(filtersSelected)

        // act
        libraryViewModel.loadLibrary(queryParameters, "fifa")

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenSuccessResponse whenLoadlibraryMore shouldReturnLibraryList`() {
        // arrange
        val responseJson = getJson("mockjson/library/get_library_success.json")
        mockResponse200(responseJson)
        val collectionType = object : TypeToken<BaseModel<LibraryResponse>>() {}.type
        val responseObject: BaseModel<LibraryResponse> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)
        val expected = ViewStateModel(
            status = ViewStateModel.Status.SUCCESS,
            model = responseObject.data.list
        )
        libraryViewModel.getLibraryMore().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status
            )
        }

        // act
        libraryViewModel.loadLibraryMore()

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `givenError400Response whenLoadlibrary shouldReturnLibraryList`() {
        // arrange
        val responseJsonError = getJson("mockjson/errors/error_400.json")
        mockResponseError400()
        val responseObjectError: ErrorResponse = GsonBuilder()
            .create()
            .fromJson(responseJsonError, ErrorResponse::class.java)
        responseObjectError.statusCode = 400
        val expectedError = ViewStateModel<ArrayList<Library>>(
            status = ViewStateModel.Status.ERROR,
            model = null,
            errors = responseObjectError
        )
        libraryViewModel.getLibraryMore().observeForever {
            actual = ViewStateModel(
                model = it.peekContent().model?.list,
                status = it.peekContent().status,
                errors = it.peekContent().errors
            )
        }

        // act
        libraryViewModel.loadLibraryMore()

        //assert
        assertEquals(expectedError, actual)
    }
}