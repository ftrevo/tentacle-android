package br.com.concrete.tentacle.features.filter

import br.com.concrete.tentacle.R
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
            click(R.id.filterMenuId)
        }
    }

    @Test
    fun givenFilter_whenListItemClicked_shouldDisplayClearButton() {
        filterArrange {
            waitToDisplay(R.id.filterContent)
        }
        filterAct {
            click("Playstation 3")
        }
        filterAssert {
            isDisplayed(R.id.filterClearButtonView)
        }
    }

    @Test
    fun givenFilterClearButtonDisplayed_whenListItemsUnmarked_shouldDisappearClearButton() {
        filterArrange {
            waitToDisplay(R.id.filterContent)
            click("Playstation 3")
        }
        filterAct {
            click("Playstation 3")
        }
        filterAssert {
            isNotDisplayed(R.id.filterClearButtonView)
        }
    }

    @Test
    fun givenFilterWithClickedItems_whenClearFilterButtonPressed_shouldReturnToMainListing() {
        filterArrange {
            mockResponse(mockWebServer)
            waitToDisplay(R.id.filterContent)
            click("Playstation 3")
            click("Playstation 4")
        }
        filterAct {
            click(R.id.filterClearButtonView)
        }
        filterAssert {
            isDisplayed(R.id.list)
        }
    }

    @Test
    fun givenFilterWithClickedItems_whenFilterButtonPressed_shouldReturnToMainFilteredListing() {
        filterArrange {
            mockResponse(mockWebServer)
            waitToDisplay(R.id.filterContent)
            click("Playstation 3")
            click("Playstation 4")
        }
        filterAct {
            click(R.id.filterButtonView)
        }
        filterAssert {
            isDisplayed(R.id.list)
        }
    }
}