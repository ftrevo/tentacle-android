package br.com.concrete.tentacle.features.registerGame

import android.os.Bundle
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity

class RegisterGameHostActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_game_host)
    }

    override fun onResume() {
        super.onResume()
        setupToolbar()
    }
}