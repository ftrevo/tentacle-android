package br.com.concrete.tentacle.features.filter

import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.features.library.LibraryFragment
import org.junit.Before
import org.junit.Test

class FilterDialogFragmentTest : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = LibraryFragment()
    }

    @Before
    fun setFragment() {
        filterArrange {
            mockResponse(mockWebServer)
        }
        filterAct{
            clickMenu()
        }
    }

    @Test
    fun givenFilter_whenListItemClicked_shouldDisplayClearButton() {
        filterAct {
            waitFilterDisplay()
            clickPS3()
        }
        filterAssert {
            clearButtonIsDisplayed()
        }
    }

    @Test
    fun givenFilterClearButtonDisplayed_whenListItemsUnmarked_shouldDisappearClearButton() {
        filterAct {
            waitFilterDisplay()
            clickPS3()
            clickPS3()
        }
        filterAssert {
            clearButtonIsNotDisplayed()
        }
    }

    @Test
    fun givenFilterWithClickedItems_whenClearFilterButtonPressed_shouldReturnToMainListing() {
        filterArrange {
            mockResponse(mockWebServer)
        }
        filterAct {
            waitFilterDisplay()
            clickPS3()
            clickPS4()
            clickClearButton()

        }
        filterAssert {
            listIsDisplayed()
        }
    }

    @Test
    fun givenFilterWithClickedItems_whenFilterButtonPressed_shouldReturnToMainFilteredListing() {
        filterArrange {
            mockResponse(mockWebServer)
        }
        filterAct {
            waitFilterDisplay()
            clickPS3()
            clickPS4()
            clickFilterButton()
        }
        filterAssert {
            listIsDisplayed()
        }
    }
}