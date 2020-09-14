package br.com.concrete.tentacle.features.filter

import br.com.concrete.tentacle.base.BaseFragmentTest
import org.junit.Test

class FilterDialogFragmentTest : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = fragment
    }

    @Test
    fun givenFilter_whenListItemClicked_shouldDisplayClearButton() {
        arrange {
            initFragment()
        }
        act {
            waitFilterDisplay()
            clickPS3()
        }
        assert {
            clearButtonIsDisplayed()
        }
    }

    @Test
    fun givenFilterClearButtonDisplayed_whenListItemsUnmarked_shouldDisappearClearButton() {
        arrange {
            initFragment()
        }
        act {
            waitFilterDisplay()
            clickPS3()
            clickPS3()
        }
        assert {
            clearButtonIsNotDisplayed()
        }
    }

    @Test
    fun givenFilterWithClickedItems_whenClearFilterButtonPressed_shouldReturnToMainListing() {
        arrange {
            initFragment()
        }
        act {
            waitFilterDisplay()
            clickPS3()
            clickPS4()
            clickClearButton()

        }
        assert {
            isDialogDismissed()
        }
    }

    @Test
    fun givenFilterWithClickedItems_whenFilterButtonPressed_shouldReturnToMainFilteredListing() {
        arrange {
            initFragment()
        }
        act {
            waitFilterDisplay()
            clickPS3()
            clickPS4()
            clickFilterButton()
        }
        assert {
            isDialogDismissed()
        }
    }
}