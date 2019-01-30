package br.com.concrete.tentacle.testing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.concrete.tentacle.R
import kotlinx.android.synthetic.main.activity_host.*

class BottomBarTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_bar_test)

        bottomBar.startListener {
            // do nothing
        }
    }
}