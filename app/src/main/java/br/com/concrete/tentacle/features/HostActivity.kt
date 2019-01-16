package br.com.concrete.tentacle.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.utils.LogWrapper
import kotlinx.android.synthetic.main.activity_host.bottomBar

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        bottomBar.startListener { action ->
            when (action) {
                R.id.action_library -> LogWrapper.log("ACTION", "Library")
                R.id.action_games -> navigateTo(R.id.navigate_to_my_games)
                R.id.action_home -> navigateTo(R.id.navigate_to_home)
                R.id.action_reservation -> LogWrapper.log("ACTION", "Wallet")
                R.id.action_events -> LogWrapper.log("ACTION", "Calendar")
            }
        }
    }

    private fun navigateTo(destination: Int){
        Navigation.findNavController(this@HostActivity, R.id.host_fragment).navigate(destination)
    }
}
