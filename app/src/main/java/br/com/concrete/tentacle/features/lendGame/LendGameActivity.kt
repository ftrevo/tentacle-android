package br.com.concrete.tentacle.features.lendGame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.concrete.tentacle.R

class LendGameActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lend_game)
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btLendGame -> lendGame()
        }
    }

    private fun lendGame() {

    }
}