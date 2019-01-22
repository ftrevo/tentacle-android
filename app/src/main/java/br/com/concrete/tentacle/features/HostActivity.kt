package br.com.concrete.tentacle.features

import android.os.Bundle
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.extensions.navigateTo
import br.com.concrete.tentacle.features.home.HomeFragment
import br.com.concrete.tentacle.features.loadmygames.LoadMyGamesFragment
import br.com.concrete.tentacle.utils.LogWrapper
import kotlinx.android.synthetic.main.activity_host.*

class HostActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        startNavListener()
        initHomeFragment()
        setupToolbar(R.drawable.ic_logo_actionbar)
    }

    private fun initHomeFragment() {
        navigateTo(R.id.container, HomeFragment.newInstance())
    }

    private fun startNavListener() {
        bottomBar.startListener { action ->
            when (action) {
                R.id.action_library -> LogWrapper.log("ACTION", "Library")
                R.id.action_games -> {
                    navigateTo(R.id.container, LoadMyGamesFragment.newInstance())
                }
                R.id.action_home -> {
                    navigateTo(R.id.container, HomeFragment.newInstance())
                }
                R.id.action_reservation -> LogWrapper.log("ACTION", "Wallet")
                R.id.action_events -> LogWrapper.log("ACTION", "Calendar")
            }
        }
    }
}
