package br.com.concrete.tentacle.features.gameViewComponent

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.base.BaseInstrumentedTest
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.testing.GameViewTestActivity
import com.google.gson.GsonBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameViewTest : BaseInstrumentedTest() {

    @get:Rule
    var activityTestRule = object : ActivityTestRule<GameViewTestActivity>(GameViewTestActivity::class.java) {}

    @Test
    fun loadGame() {

        val json = "mockjson/game/game.json".getJson()
        val gson = GsonBuilder().create()
    }
}