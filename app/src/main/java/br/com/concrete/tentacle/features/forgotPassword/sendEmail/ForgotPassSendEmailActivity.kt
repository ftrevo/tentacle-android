package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import android.app.Activity
import android.os.Bundle
import android.view.View
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.validateEmail
import kotlinx.android.synthetic.main.activity_forgot_pass_send_email.btSend
import kotlinx.android.synthetic.main.activity_forgot_pass_send_email.edtEmail
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.edt

class ForgotPassSendEmailActivity : Activity(), View.OnClickListener {

    companion object {
        const val EMAIL = "EMAIL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_send_email)

        init()
        initListener()
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
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btSend -> verifyData()
        }
    }

    private fun verifyData() {
        val email = edtEmail.getText()

        when (email.validateEmail()) {
            true -> sendEmailViewModel()
            false -> showError()
        }
    }

    private fun sendEmailViewModel() {
        // TODO CALL VIEW MODEL HERE
        edtEmail.showError(false)
    }

    private fun showError() {
        edtEmail.showError(true)
    }
}
