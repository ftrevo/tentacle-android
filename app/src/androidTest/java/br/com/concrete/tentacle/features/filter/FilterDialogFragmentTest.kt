package br.com.concrete.tentacle.features.filter

import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.base.BaseInstrumentedTest
import br.com.concrete.tentacle.testing.SingleFragmentTestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FilterDialogFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(SingleFragmentTestActivity::class.java)

    @Before
    fun initFragment(){
        activityRule.activity.setFragment(fragment)
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

    @Test
    fun givenFilter_whenCloseButtonPressed_shouldReturnToMainListing() {
        arrange {
            initFragment()
        }
        act {
            waitFilterDisplay()
            clickCloseButton()
        }
        assert {
            isDialogDismissed()
        }
    }

}