package br.com.concrete.tentacle.features.gameViewComponent

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.actions.SetTextInTextView
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.testing.GameViewTestActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.debug.activity_game_view_test.gameView
import org.hamcrest.CoreMatchers.not


fun arrange(action: GameViewArrange.() -> Unit) {
    GameViewArrange().apply(action)
}

fun act(action: GameViewAct.() -> Unit) {
    GameViewAct().apply(action)
}

fun assert(action: GameViewAssert.() -> Unit) {
    GameViewAssert().apply(action)
}

class GameViewArrange {

    fun setupGame(activityTestRule: ActivityTestRule<GameViewTestActivity>) {
        val game = getGame()
        setGame(activityTestRule, game)
    }

    private fun getGame() =
        GsonBuilder()
            .create()
            .fromJson(
                "mockjson/game/load_game_success.json".getJson(),
                Game::class.java
            )


    private fun setGame(activityTestRule: ActivityTestRule<GameViewTestActivity>, game: Game) {
        UiThreadStatement.runOnUiThread {
            activityTestRule.activity.gameView.setGame(game)
        }
    }

    fun showGameStatus(activityTestRule: ActivityTestRule<GameViewTestActivity>) {
        UiThreadStatement.runOnUiThread {
            activityTestRule.activity.gameView.showStatusView(true)
        }
    }

    fun hideGameStatus(activityTestRule: ActivityTestRule<GameViewTestActivity>) {
        UiThreadStatement.runOnUiThread {
            activityTestRule.activity.gameView.showStatusView(false)
        }
    }

}

class GameViewAct {

    fun setStatusText() {
        onView(withId(R.id.tvGameStatus))
            .perform(SetTextInTextView("Pendente"))
    }


}

class GameViewAssert {

    fun isGameRateDisplayed() {
        isDisplayed(R.id.rbGame)
    }

    fun areGameModesCorrectlyDisplayed() {
        isDisplayed("Single player")
        isDisplayed("Shooter")
        isDisplayed("Adventure")
        isDisplayed("Multiplayer")
    }

    fun isGameNameCorrect() {
        hasText(R.id.tvGameName, "The Last of Us Remastered")
    }

    fun isRealeaseDateCorrect() {
        hasText(R.id.tvGameReleaseYear, "2014")
    }

    fun areStatusDisplayed() {
        isDisplayed(R.id.tvGameStatus)
        isDisplayed(R.id.ivGameStatus)
    }

    fun areStatusNotDisplayed() {
        isNotDisplayed(R.id.tvGameStatus)
        isNotDisplayed(R.id.ivGameStatus)
    }

    private fun isDisplayed(text: String) {
        onView(withText(text))
            .perform(scrollTo())
            .check(matches(isDisplayed()))
    }

    private fun isNotDisplayed(id: Int) {
        onView(withId(id))
            .check(matches(not(isDisplayed())))
    }

    private fun isDisplayed(id: Int) {
        onView(withId(id))
            .perform(scrollTo())
            .check(matches(isDisplayed()))
    }

    private fun hasText(id: Int, text: String) {
        onView(withId(id))
            .perform(scrollTo())
            .check(matches(withText(text)))
    }

}