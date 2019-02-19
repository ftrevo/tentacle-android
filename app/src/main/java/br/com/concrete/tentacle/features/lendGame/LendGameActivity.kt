package br.com.concrete.tentacle.features.lendGame

import android.os.Bundle
import android.view.View
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity


class LendGameActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val MEDIA_OBJECT = "MEDIA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lend_game)
        setupToolbar(R.string.toolbar_title_lend_game, R.drawable.ic_close, true)
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btLendGame -> lendGame()
        }
    }

    private fun lendGame() {

    }
}