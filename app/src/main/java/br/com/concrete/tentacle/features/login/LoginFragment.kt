package br.com.concrete.tentacle.features.login


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.callSnackbar
import br.com.concrete.tentacle.extensions.validateEmail
import br.com.concrete.tentacle.extensions.validatePassword
import br.com.concrete.tentacle.features.register.RegisterActivity
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(), View.OnClickListener {

    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var views: View

    private val viewModel: LoginViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_login, container, false)

        views.btLogin.setOnClickListener(this)
        views.tvRegisterAccount.setOnClickListener(this)

        views.edtEmail.edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if(email.isNotEmpty()){
                    views.edtEmail.showError(!email.validateEmail())
                }else{
                    views.edtEmail.showError(false)
                }
            }
        })

        views.edtPassword.edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if(password.isNotEmpty()){
                    views.edtPassword.showError(!password.validatePassword())
                }else{
                    views.edtPassword.showError(false)
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
                    ViewStateModel.Status.SUCCESS -> {
                        Log.d("LOGIN-SUCCESS", "User logged")
                        // TODO - dismiss progressBar
                        // TODO - save session (viewStateModel.model)
                        // TODO - get user
                        callActivity()
                        views.btLogin.isLoading(false)
                        enableField(true)
                    }
                    ViewStateModel.Status.LOADING -> {
                        views.btLogin.isLoading(true)
                        enableField(false)
                    }
                    ViewStateModel.Status.ERROR -> {
                        // TODO - dismiss progressBar
                        // TODO - on error
                        Log.d("LOGIN-ERROR", "User logged")
                        views.btLogin.isLoading(false)
                        enableField(true)
                        context?.callSnackbar(views, getString(R.string.error_login))
                    }
                }
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun callActivity() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btLogin -> handleLogin()
            R.id.tvRegisterAccount -> showRegisterAccount()
        }
    }

    private fun showRegisterAccount(){ val t : EditText
        startActivity(Intent(context, RegisterActivity::class.java))
    }

    private fun handleLogin() {
        val email = views.edtEmail.getText()
        val password = views.edtPassword.getText()

        val isOk = email.validateEmail() && password.validatePassword()

        when (isOk) {
            true -> loginViewModel.loginUser(email, password)
            false -> context?.callSnackbar(views, getString(R.string.verificar_campos_login))
        }
    }


    private fun enableField(enableField: Boolean) {
        views.edtEmail.isEnabled = enableField
        views.edtPassword.isEnabled = enableField
    }

}
