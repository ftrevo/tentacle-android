package br.com.concrete.tentacle.testing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.Game
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.debug.activity_game_view_test.gameView


class GameViewTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view_test)

        val json = assets.open("mockjson/game/game.json").bufferedReader().use { it.readText() }


        val gson = GsonBuilder().create()
        val game = gson.fromJson(json, Game::class.java)

        gameView.setGame(game)


    }
}