package br.com.concrete.tentacle.features.menu

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.BuildConfig
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.features.library.loan.LoanActivity
import br.com.concrete.tentacle.features.login.LoginActivity
import br.com.concrete.tentacle.features.profile.ProfileActivity
import br.com.concrete.tentacle.utils.DialogUtils
import br.com.concrete.tentacle.utils.LogWrapper
import kotlinx.android.synthetic.main.fragment_menu.logout
import kotlinx.android.synthetic.main.fragment_menu.name
import kotlinx.android.synthetic.main.fragment_menu.profile
import kotlinx.android.synthetic.main.fragment_menu.state
import kotlinx.android.synthetic.main.fragment_menu.version
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MenuFragment : Fragment() {

    private val menuViewModel: MenuViewModel by viewModel()
    private val sharePrefRepository: SharedPrefRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initObservable()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObservable(){
        menuViewModel.getUser().observe(this, Observer { base ->
            when (base.status) {
                ViewStateModel.Status.SUCCESS -> {
                    base.model?.let { updateUI(it) }
                }
                else -> base.errors?.message?.let {
                    LogWrapper.log("A problem happens", it[0])
                }
            }
        })

        menuViewModel.loadUser()
    }

    fun init(){
        logout.setOnClickListener { checkLogout() }
        profile.setOnClickListener { goToProfile() }
        version.text = String.format(getString(R.string.version), BuildConfig.VERSION_NAME)
    }

    private fun goToProfile(){
        activity?.launchActivity<ProfileActivity>(
            animation = ActivityAnimation.TRANSLATE_UP
        )
    }

    private fun updateUI(user: User){
        name.text = user?.name
        state.text = user?.state?.name
    }

    private fun checkLogout() {
        activity?.let {
            DialogUtils.showDialog(
                context = it,
                title = getString(R.string.logout_title),
                message = getString(R.string.logout_question),
                positiveText = getString(R.string.ok),
                positiveListener = DialogInterface.OnClickListener { _, _ ->
                    performLogout()
                },
                negativeText = getString(R.string.cancel)
            )
        }
    }

    private fun performLogout() {
        sharePrefRepository.removeSession()
        sharePrefRepository.removeUser()
        val login = Intent(activity, LoginActivity::class.java)
        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(login)
        activity?.finish()
    }

}
