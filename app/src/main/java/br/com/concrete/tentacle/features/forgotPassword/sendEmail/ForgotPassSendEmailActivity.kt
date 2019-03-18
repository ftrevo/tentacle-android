package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.validateEmail
import br.com.concrete.tentacle.features.forgotPassword.passwordRecovery.ARGUMENT_EMAIL
import br.com.concrete.tentacle.features.forgotPassword.passwordRecovery.PasswordRecoveryActivity
import kotlinx.android.synthetic.main.activity_forgot_pass_send_email.btSend
import kotlinx.android.synthetic.main.activity_forgot_pass_send_email.edtEmail
import kotlinx.android.synthetic.main.activity_forgot_pass_send_email.tvAlreadyHaveToken
import kotlinx.android.synthetic.main.button_back_arrow.backArrowButton
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.edt
import org.koin.android.viewmodel.ext.android.viewModel

class ForgotPassSendEmailActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val EMAIL = "EMAIL"
    }

    private val forgotPassSendEmailViewModel: ForgotPassSendEmailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_send_email)

        init()
        initListener()
        initObserver()
    }

    private fun init() {
        if (intent.hasExtra(EMAIL)) {
            val email = intent.extras?.getString(EMAIL)
            email?.let {
                edtEmail.edt.setText(it)
            }
        }
    }

    private fun initListener() {
        btSend.setOnClickListener(this)
        tvAlreadyHaveToken.setOnClickListener(this)
        backArrowButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btSend -> verifyData()
            R.id.tvAlreadyHaveToken -> callNextActivity()
            R.id.backArrowButton -> onBackPressed()
        }
    }

    private fun verifyData() {
        val email = edtEmail.getText()

        when (email.validateEmail()) {
            true -> sendEmailViewModel(email)
            false -> showError()
        }
    }

    private fun sendEmailViewModel(email: String) {
        forgotPassSendEmailViewModel.forgotPassword(email)
        edtEmail.showError(false)
    }

    private fun showError() {
        edtEmail.showError(true)
    }

    private fun initObserver() {
        forgotPassSendEmailViewModel.getStateModel().observe(this, Observer { state ->
            when (state.status) {
                ViewStateModel.Status.LOADING -> {
                    btSend.isLoading(true)
                }
                ViewStateModel.Status.SUCCESS -> {
                    btSend.isLoading(false)
                    callNextActivity()
                }
                ViewStateModel.Status.ERROR -> {
                    btSend.isLoading(false)
                    showError(state.errors, getString(R.string.ops_ocorreu_erro))
                }
            }
        })
    }

    private fun callNextActivity() {
        val intent = Intent(applicationContext, PasswordRecoveryActivity::class.java)
        intent.putExtra(ARGUMENT_EMAIL, edtEmail.getText())
        startActivity(intent)
    }
}
