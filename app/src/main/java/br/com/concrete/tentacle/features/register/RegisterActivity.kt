package br.com.concrete.tentacle.features.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.finishActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishActivity(animation = ActivityAnimation.TRANSLATE_RIGHT)
    }

}