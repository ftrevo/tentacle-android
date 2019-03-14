package br.com.concrete.tentacle.features.gameViewComponent

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.actions.SetTextInTextView
import br.com.concrete.tentacle.base.BaseInstrumentedTest
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.testing.GameViewTestActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.debug.activity_game_view_test.gameView
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameViewTest : BaseInstrumentedTest() {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<GameViewTestActivity>(GameViewTestActivity::class.java) {}

    @Before
    fun setupGame() {
        val json = "mockjson/game/load_game_success.json".getJson()
        val gson = GsonBuilder().create()

        val game = gson.fromJson(json, Game::class.java)

        UiThreadStatement.runOnUiThread {
            activityTestRule.activity.gameView.setGame(game)
        }
    }

    @Test
    fun loadGameWithoutStatus() {
        loadGames(false)
    }

    @Test
    fun loadGameWithStatus() {
        loadGames(true)
    }

    private fun loadGames(showStatus: Boolean) {
        UiThreadStatement.runOnUiThread {
            activityTestRule.activity.gameView.showStatusView(showStatus)
        }

        Espresso.onView(withId(R.id.tvGameName))
            .perform(scrollTo())
            .check(ViewAssertions.matches(withText("The Last of Us Remastered")))

        Espresso.onView(withId(R.id.rbGame))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.tvGameReleaseYear))
            .perform(scrollTo())
            .check(matches(withText("2014")))

        Espresso.onView(withText("Single player"))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        Espresso.onView(withText("Multiplayer"))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        Espresso.onView(withText("Shooter"))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        Espresso.onView(withText("Adventure"))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        Espresso.onView(withText("Multiplayer"))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        val assertion: ViewAssertion?
        if (showStatus) {
            assertion = matches(isDisplayed())
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            Espresso.onView(withId(R.id.tvGameStatus))
                .perform(SetTextInTextView("Pendente"))
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        } else {
            assertion = matches(not(isDisplayed()))
        }

        val tvGameStatus = Espresso.onView(ViewMatchers.withId(R.id.tvGameStatus))
        val ivGameStatus = Espresso.onView(ViewMatchers.withId(R.id.ivGameStatus))

        if (showStatus) {
            tvGameStatus.perform(scrollTo())
            ivGameStatus.perform(scrollTo())
        }

        tvGameStatus.check(assertion)
        ivGameStatus.check(assertion)
    }
}