package br.com.concrete.tentacle.features

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.utils.LogWrapper
import kotlinx.android.synthetic.main.activity_host.*

class HostActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        startNavListener()
        setupToolbar(R.drawable.ic_logo_actionbar)
    }

    private fun startNavListener() {
        val navController = Navigation.findNavController(this, R.id.garden_nav_fragment)
        val topLevelDestinations = setOf(R.id.home, R.id.myGames)
        val appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomBar.startListener { action ->
            when (action) {
                R.id.action_library -> LogWrapper.log("ACTION", "Library")
                R.id.action_games -> {
                    navController.navigate(R.id.navigate_to_my_games)
                }
                R.id.action_home -> {
                    navController.navigate(R.id.navigate_to_home)
                }
                R.id.action_reservation -> LogWrapper.log("ACTION", "Wallet")
                R.id.action_events -> LogWrapper.log("ACTION", "Calendar")
            }
        }
    }



    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
