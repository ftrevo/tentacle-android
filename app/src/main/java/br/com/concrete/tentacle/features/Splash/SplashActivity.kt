package br.com.concrete.tentacle.features.Splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.features.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launchActivity<LoginActivity>()
        finish()
    }
}