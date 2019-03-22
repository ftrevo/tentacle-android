package br.com.concrete.tentacle.features

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.interfaces.CallBack
import br.com.concrete.tentacle.utils.LogWrapper
import kotlinx.android.synthetic.main.activity_host.bottomBar
import kotlinx.android.synthetic.main.activity_host.drawer_layout
import kotlinx.android.synthetic.main.activity_host.toolbar


class HostActivity : BaseActivity(), CallBack {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        init()
    }

    private fun init() {
        setSupportActionBarWithIcon(toolbar, R.string.toolbar_title_home, R.drawable.ic_logo_actionbar)
        initObservable()
        startNavListener()
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
        val topLevelDestinations = setOf(R.id.home, R.id.myGames, R.id.library, R.id.my_reservations)
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
        setupToolbar(R.drawable.ic_logo_actionbar)
        val mDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close)
        drawer_layout.addDrawerListener(mDrawerToggle)
    }

    override fun changeBottomBar(actionNameId: Int, navigateId: Int) {
        bottomBar.updateBottomBar(actionNameId)
        navController.navigate(navigateId)
    }

    override fun onBackPressed() {
        finish()
    }
}
