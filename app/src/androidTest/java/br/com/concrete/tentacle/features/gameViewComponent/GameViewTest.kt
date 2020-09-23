package br.com.concrete.tentacle.features.gameViewComponent

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.testing.GameViewTestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameViewTest {

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
    fun givenGameLoaded_whenStartActivity_shouldDisplayFieldsProperly() {
        assert {
            areGameModesCorrectlyDisplayed()
            isGameRateDisplayed()
            isGameNameCorrect()
            isRealeaseDateCorrect()
        }
    }

    @Test
    fun givenHiddenGameStatus_wnehloadGame_shouldNotDisplayIt() {
        arrange {
            hideGameStatus(activityTestRule)
        }
        assert {
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
            areStatusDisplayed()
        }
    }
}