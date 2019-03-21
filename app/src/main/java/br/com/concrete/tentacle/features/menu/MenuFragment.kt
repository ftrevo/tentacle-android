package br.com.concrete.tentacle.features.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.BuildConfig
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import kotlinx.android.synthetic.main.fragment_menu.*
import org.koin.android.viewmodel.ext.android.viewModel

class MenuFragment : Fragment(), View.OnClickListener {

    private val menuViewModel: MenuViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initObservable()
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    private fun initObservable(){
        menuViewModel.getUser().observe(this, Observer { base ->
            when (base.status) {
                ViewStateModel.Status.SUCCESS -> {
                    base.model?.let { updateUI(it) }
                }
                ViewStateModel.Status.ERROR -> {
                    callError(base.errors)
                }
            }
        })

        menuViewModel.loadUser()
    }

    private fun updateUI(user: User){
        version.text = String.format(getString(R.string.version), BuildConfig.VERSION_NAME)
        name.text = user.name
        state.text = user.state.name
    }

    private fun callError(errors: ErrorResponse?) {
        errors?.let {

        }
    }


    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

}
