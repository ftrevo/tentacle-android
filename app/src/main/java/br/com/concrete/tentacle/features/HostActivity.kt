package br.com.concrete.tentacle.features

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.interfaces.CallBack
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.features.login.LoginActivity
import br.com.concrete.tentacle.utils.DialogUtils
import br.com.concrete.tentacle.utils.LogWrapper
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_host.*
import org.koin.android.ext.android.inject

class HostActivity : BaseActivity(), CallBack {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        init()
        startNavListener()
        initObservable()
    }

    private fun init(){
        setSupportActionBarWithIcon(toolbar, R.string.toolbar_title_home, R.drawable.ic_logo_actionbar)
    }

    companion object {
        val fragment: MutableLiveData<Int> = MutableLiveData()
    }

    private fun initObservable() {
        HostActivity.fragment.observe(this, Observer {
            initOn(it)
        })
    }

    private fun initOn(id: Int) {
        when (id) {
            R.id.navigate_to_library -> {
                changeBottomBar(R.id.action_library, R.id.navigate_to_library)
            }
            R.id.navigate_to_my_games -> {
                changeBottomBar(R.id.action_games, R.id.navigate_to_my_games)
            }
            R.id.navigate_to_home -> {
                changeBottomBar(R.id.action_home, R.id.navigate_to_home)
            }
            R.id.navigate_to_my_reservations -> {
                changeBottomBar(R.id.action_reservation, R.id.navigate_to_my_reservations)
            }
            R.id.action_events -> {}
        }
    }

    private fun startNavListener() {
        navController = Navigation.findNavController(this, R.id.nav_fragment)
        val topLevelDestinations = setOf(R.id.home, R.id.myGames, R.id.library)
        val appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomBar.startListener { action ->
            when (action) {
                R.id.action_library -> {
                    navController.navigate(R.id.navigate_to_library)
                }
                R.id.action_games -> {
                    navController.navigate(R.id.navigate_to_my_games)
                }
                R.id.action_home -> {
                    navController.navigate(R.id.navigate_to_home)
                }
                R.id.action_reservation -> {
                    navController.navigate(R.id.navigate_to_my_reservations)
                }
                R.id.action_events -> LogWrapper.log("ACTION", "Calendar")
            }
            setupToolbar(R.drawable.ic_logo_actionbar)
        }
    }

    override fun changeBottomBar(actionNameId: Int, navigateId: Int) {
        bottomBar.updateBottomBar(actionNameId)
        navController.navigate(navigateId)
    }

    override fun onBackPressed() {
        finish()
    }
}
