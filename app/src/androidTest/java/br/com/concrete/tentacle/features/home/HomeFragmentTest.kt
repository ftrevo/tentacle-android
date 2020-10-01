package br.com.concrete.tentacle.features.home

import androidx.test.espresso.intent.rule.IntentsTestRule
import br.com.concrete.tentacle.testing.SingleFragmentTestActivity
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * For more usages of RecyclerViewMatcher check the link below:
 * https://spin.atomicobject.com/2016/04/15/espresso-testing-recyclerviews/
 */

class HomeFragmentTest {

    @get:Rule
    var intentsTestRule = IntentsTestRule(SingleFragmentTestActivity::class.java)

    val mockWebServer = MockWebServer()

    @Before
    fun setupServer() {
        mockWebServer.start(8080)
        initHomeFragment()
    }

    private fun initHomeFragment() {
        intentsTestRule.activity.setFragment(HomeFragment())
    }

    @After
    fun shutdownServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenSuccessResponse_whenStartFragment_shouldShowListCorrectly() {
        arrange {
            mockNewHomeGamesResponse(mockWebServer)
        }
        assert {
            isListDisplayed()
            isListErrorNotDisplayed()
            checkListItemsTitle()
            checkListItemSummary()
            checkListItemsPlatforms()
        }
    }

    @Test
    fun givenNoGamesRegistered_whenStartFragment_shouldShowNoGamesRegisteredMessage() {
        arrange {
            mockLoadHomeGames(mockWebServer)
        }
        assert {
            isListErrorDisplayed()
            isListNotDisplayed()
            checkNoRegisteredGameErrorDescription()
            isRegisterButtonDisplayedProperly()
        }
    }

    @Test
    fun givenErrorResponse_whenStartFragment_shouldShowErrorMessage() {
        arrange {
            mockErrorResponse(mockWebServer)
        }
        assert {
            isListErrorDisplayed()
            isLoadAgainMessageDisplayed()
            isListNotDisplayed()
            checkGenericErrorDescription()
        }
    }

    @Test
    fun givenSuccessAfterErrorMessage_whenClickErrorButton_shouldDisplayList() {
        arrange {
            mockErrorResponse(mockWebServer)
            mockNewHomeGamesResponse(mockWebServer)
        }
        act {
            clickErrorButton()
        }
        assert {
            isListDisplayed()
            isListErrorNotDisplayed()
        }
    }

    @Test
    fun givenErrorAfterErrorResponse_whenClickErrorButton_shouldShowErrorMessage() {
        arrange {
            mockErrorResponse(mockWebServer)
            mockErrorResponse(mockWebServer)
        }
        act {
            clickErrorButton()
        }
        assert {
            isListErrorDisplayed()
            isLoadAgainMessageDisplayed()
            isListNotDisplayed()
            checkGenericErrorDescription()
        }
    }

    @Test
    fun givenUpdateError_whenStartFragment_shouldShowErrorMessage() {
        arrange {
            mockUpdateErrorResponse(mockWebServer)
        }
        assert {
            isErrorMessageDisplayed()
        }
    }
}