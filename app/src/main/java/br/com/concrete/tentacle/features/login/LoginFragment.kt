package br.com.concrete.tentacle.features.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.callSnackbar
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.extensions.validateEmail
import br.com.concrete.tentacle.extensions.validatePassword
import br.com.concrete.tentacle.features.HostActivity
import br.com.concrete.tentacle.features.forgotPassword.sendEmail.ForgotPassSendEmailActivity
import br.com.concrete.tentacle.features.register.RegisterActivity
import br.com.concrete.tentacle.utils.LogWrapper
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.edt
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment(), View.OnClickListener {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        handleWithSession()

        initEvents()

        initListeners()

        initViewModel()
    }

    private fun initViewModel() {
        loginViewModel.getStateModel().observe(this, Observer { viewState ->
            viewState.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        LogWrapper.log("LOGIN-SUCCESS", "User logged")
                        handleWithSession()
                        setLoading(false)
                    }
                    ViewStateModel.Status.LOADING -> {
                        setLoading(true)
                    }
                    ViewStateModel.Status.ERROR -> {
                        LogWrapper.log("LOGIN-ERROR", "User logged")
                        setLoading(false)
                        showError(it.errors, getString(R.string.was_some_mistake))
                    }
                }
            }
        })
        lifecycle.addObserver(loginViewModel)
    }

    private fun setLoading(loading: Boolean) {
        btLogin.isLoading(loading)
        enableField(!loading)
    }

    private fun initListeners() {
        edtEmail.edt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }

        edtPassword.edt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword()
            }
        }

        parent.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (!parent.onTouchEvent(event)) {
                    validateEmail()
                    validatePassword()
                }

                return true
            }
        })

        imgBackground.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (!parent.onTouchEvent(event)) {
                    validateEmail()
                    validatePassword()
                } else if (event?.action == MotionEvent.ACTION_DOWN ||
                    event?.action == MotionEvent.ACTION_UP
                ) {
                    validateEmail()
                    validatePassword()
                }

                return true
            }
        })
    }

    private fun validateEmail() {
        val email = edtEmail.edt.text.toString()
        if (email.isNotEmpty()) {
            edtEmail.showError(!email.validateEmail())
        } else {
            edtEmail.showError(false)
        }
    }

    private fun validatePassword() {
        val password = edtPassword.edt.text.toString()
        if (password.isNotEmpty()) {
            edtPassword.showError(!password.validatePassword())
        } else {
            edtPassword.showError(false)
        }
    }

    private fun initEvents() {
        btLogin.setOnClickListener(this)
        tvRegisterAccount.setOnClickListener(this)
        tvForgotPassword.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btLogin -> handleLogin()
            R.id.tvRegisterAccount -> showRegisterAccount()
            R.id.tvForgotPassword -> callForgotPassword()
        }
    }

    private fun showRegisterAccount() {
        activity?.launchActivity<RegisterActivity>(animation = ActivityAnimation.TRANSLATE_LEFT)
    }

    private fun handleLogin() {

        val email = edtEmail.getText()
        val password = edtPassword.getText()

        var isOk = true

        if (!email.validateEmail()) {
            isOk = false
            edtEmail.showError(true)
        }
        if (!password.validatePassword()) {
            isOk = false
            edtPassword.showError(true)
        }

        when (isOk) {
            true -> loginViewModel.loginUser(email, password)
            false -> context?.callSnackbar(view!!, getString(R.string.verificar_campos_login))
        }
    }

    private fun enableField(enableField: Boolean) {
        edtEmail.edt.isEnabled = enableField
        edtPassword.edt.isEnabled = enableField
    }

    private fun handleWithSession() {
        if (loginViewModel.isUserLogged()) {
            startActivity(Intent(context, HostActivity::class.java))
            activity?.finish()
        }
    }

    override fun getToolbarTitle(): Int {
        return 0
    }

    private fun callForgotPassword() {
        val bundle = Bundle()
        bundle.putString(ForgotPassSendEmailActivity.EMAIL, edtEmail.getText())
        activity?.launchActivity<ForgotPassSendEmailActivity>(extras = bundle, animation = ActivityAnimation.TRANSLATE_UP)
    }
}
