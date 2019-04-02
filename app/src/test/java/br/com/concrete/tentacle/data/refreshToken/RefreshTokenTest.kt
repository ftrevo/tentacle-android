package br.com.concrete.tentacle.data.refreshToken

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.ViewStateModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.koin.standalone.inject
import java.net.HttpURLConnection

class RefreshTokenTest : BaseViewModelTest() {

    private val refreshTokenRequestMock: RefreshTokenRequestMock by inject()

    @Test
    fun `when get any request error except 401 should return error`() {
        val observerStatusExpected = ViewStateModel.Status.ERROR

        mockResponseError400()

        var isError400 = false
        refreshTokenRequestMock.getProfileViewState().observeForever {
            assertEquals(observerStatusExpected, it.status)

            it.errors?.let { errorResponse ->
                isError400 = errorResponse.statusCode == HttpURLConnection.HTTP_BAD_REQUEST
            } ?: run {
                isError400 = false
            }
        }

        refreshTokenRequestMock.executeRequest()
        assertTrue(isError400)
    }

    @Test
    fun `when get error 401 should do a refreshToken successfully and retry the request`() {
        val observerStatusExpected = ViewStateModel.Status.SUCCESS

        mockResponseError401()

        val responseRefreshTokenSuccessJson = getJson("mockjson/login/login_success.json")
        mockResponse200(responseRefreshTokenSuccessJson)

        val reponseRetryRequestSuccess = getJson("mockjson/profile/get_profile_success.json")
        mockResponse200(reponseRetryRequestSuccess)

        var hasGetProfileSucceed = false
        refreshTokenRequestMock.getProfileViewState().observeForever {
            assertEquals(observerStatusExpected, it.status)

            hasGetProfileSucceed = it.model != null
        }

        refreshTokenRequestMock.executeRequest()
        assertTrue(hasGetProfileSucceed)
    }

    @Test
    fun `when get error 401 and refreshToken fails should return error 401`() {
        val observerStatusExpected = ViewStateModel.Status.ERROR

        mockResponseError401()

        mockResponseError400()

        var isError401 = false
        refreshTokenRequestMock.getProfileViewState().observeForever {
            assertEquals(observerStatusExpected, it.status)

            it.errors?.let { errorResponse ->
                isError401 = errorResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED
            } ?: run {
                isError401 = false
            }
        }

        refreshTokenRequestMock.executeRequest()
        assertTrue(isError401)
    }
}