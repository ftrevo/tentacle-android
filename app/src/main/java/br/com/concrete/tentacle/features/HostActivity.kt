package br.com.concrete.tentacle.features

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_host.bottomBar
import org.koin.android.ext.android.inject


class HostActivity : BaseActivity(), CallBack {

    private lateinit var navController: NavController
    private val sharePrefRepository: SharedPrefRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        startNavListener()
        setupToolbar(R.drawable.ic_logo_actionbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                checkLogout()
                return true
            }
            else -> super.onOptionsItemSelected(item)
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
                R.id.action_reservation -> LogWrapper.log("ACTION", "Wallet")
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

    private fun checkLogout(){
        DialogUtils.showDialog(
            context = this@HostActivity,
            title = getString(R.string.logout_title),
            message = getString(R.string.logout_question),
            positiveText = getString(android.R.string.ok),
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                performLogout()
            },
            negativeText = getString(android.R.string.no)
        )
    }

    private fun performLogout() {
        sharePrefRepository.removeSession()
        val login = Intent(this, LoginActivity::class.java)
        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(login)
        finish()
    }
}
