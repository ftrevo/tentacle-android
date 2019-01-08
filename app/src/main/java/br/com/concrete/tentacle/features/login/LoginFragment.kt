package br.com.concrete.tentacle.features.login


import android.content.Intent
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
import br.com.concrete.tentacle.extensions.callSnackbar
import br.com.concrete.tentacle.extensions.validateEmail
import br.com.concrete.tentacle.extensions.validatePassword
import br.com.concrete.tentacle.features.register.RegisterActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(), View.OnClickListener {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        btLogin.setOnClickListener(this)
        tvRegisterAccount.setOnClickListener(this)

        edtEmail.edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if(email.isNotEmpty()){
                    edtEmail.showError(!email.validateEmail())
                }else{
                    edtEmail.showError(false)
                }
            }
        })


        edtPassword.edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if(password.isNotEmpty()){
                    edtPassword.showError(!password.validatePassword())
                }else{
                    edtPassword.showError(false)
                }
            }
        })

        loginViewModel.getStateModel().observe(this, Observer { viewState ->
            viewState?.let {
                when(viewState.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        Log.d("LOGIN-SUCCESS", "User logged")
                        btLogin.isLoading(false)
                        enableField(true)
                    }
                    ViewStateModel.Status.LOADING -> {
                        btLogin.isLoading(true)
                        enableField(false)
                    }
                    ViewStateModel.Status.ERROR -> {
                        Log.d("LOGIN-ERROR", "User logged")
                        btLogin.isLoading(false)
                        enableField(true)
                        context?.callSnackbar(view!!, it.errors.toString())
                    }
                }
            }
        })
        lifecycle.addObserver(loginViewModel)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btLogin -> handleLogin()
            R.id.tvRegisterAccount -> showRegisterAccount()
        }
    }

    private fun showRegisterAccount(){
        startActivity(Intent(context, RegisterActivity::class.java))
    }

    private fun handleLogin() {

        val email = edtEmail.getText()
        val password = edtPassword.getText()

        val isOk = email.validateEmail() && password.validatePassword()

        when (isOk) {
            true -> loginViewModel.loginUser(email, password)
            false -> context?.callSnackbar(view!!, getString(R.string.verificar_campos_login))
        }
    }

    private fun enableField(enableField: Boolean) {
        edtEmail.edt.isEnabled = enableField
        edtPassword.edt.isEnabled = enableField
    }

}
