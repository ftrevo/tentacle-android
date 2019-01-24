package br.com.concrete.tentacle.features.registerGame

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity

class RegisterGameHostActivity: BaseActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_game_host)

        startNavController()
    }

    override fun onResume() {
        super.onResume()
        setupToolbar()
    }

    private fun startNavController() {
        navController = Navigation.findNavController(this, R.id.register_game_nav_fragment)
        setupActionBarWithNavController(navController = navController)
    }


    override fun onSupportNavigateUp() =
        navController.navigateUp()

}