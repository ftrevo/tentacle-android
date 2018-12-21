package br.com.concrete.tentacle.features.login


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.validaEmail
import br.com.concrete.tentacle.extensions.validaPassword
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(), View.OnClickListener {

    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var views: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_login, container, false)

        views.btLogin.setOnClickListener(this)

        views.edEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                when (email.validaEmail(email)) {
                    true -> views.tvErrorEmail.visibility = View.VISIBLE
                    false -> views.tvErrorEmail.visibility = View.GONE
                }
            }
        })

        views.edPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                when (password.validaPassword(password)) {
                    true -> views.tvErrorPassword.visibility = View.VISIBLE
                    false -> views.tvErrorPassword.visibility = View.GONE
                }
            }
        })

        init()

        return views
    }

    private fun init() {
        loginViewModel.getStateModel().observe(this, Observer { viewState ->
            viewState?.let {
                when(viewState.status) {
                    ViewStateModel.Status.SUCCESS -> callActivity()
                    ViewStateModel.Status.ERROR -> callSnackbar(getString(R.string.error_login))
                    ViewStateModel.Status.LOADING -> {

                    }
                }
            }
        })
    }

    private fun callActivity() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btLogin -> handleLogin()
        }
    }

    private fun handleLogin() {
        val email = views.edEmail.text.toString()
        val password = views.edPassword.text.toString()

        val isOk = !email.validaEmail(email) && !password.validaPassword(password)

        when (isOk) {
            true -> loginViewModel.loginUser(email, password)
            false -> callSnackbar(getString(R.string.verificar_campos_login))
        }
    }

    private fun callSnackbar(message: String) {
        Snackbar.make(views, message, Snackbar.LENGTH_SHORT).show()
    }

}
