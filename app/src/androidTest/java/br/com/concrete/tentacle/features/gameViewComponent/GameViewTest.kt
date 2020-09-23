package br.com.concrete.tentacle.features.gameViewComponent

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.base.BaseInstrumentedTest
import br.com.concrete.tentacle.testing.GameViewTestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameViewTest : BaseInstrumentedTest() {

    @get:Rule
    var activityTestRule =
        object : ActivityTestRule<GameViewTestActivity>(GameViewTestActivity::class.java) {}

    @Before
    fun setup() {
        arrange {
            setupGame(activityTestRule)
        }
    }

    @Test
    fun givenHiddenGameStatus_wnehloadGame_shouldNotDisplayIt() {
        arrange {
            hideGameStatus(activityTestRule)
        }
        assert {
            isGameRateDisplayed()
            areGameModesCorrectlyDisplayed()
            isGameNameCorrect()
            isRealeaseDateCorrect()
            areStatusNotDisplayed()
        }
    }

    @Test
    fun givenGameStatus_wnehloadGame_shouldDisplayIt() {
        arrange {
            showGameStatus(activityTestRule)
        }
        act {
            setStatusText()
        }
        assert {
            isGameRateDisplayed()
            areGameModesCorrectlyDisplayed()
            isGameNameCorrect()
            isRealeaseDateCorrect()
            areStatusDisplayed()
        }
    }
}