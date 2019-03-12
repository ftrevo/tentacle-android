package br.com.concrete.tentacle.features.forgotPassword.sendEmail

import android.app.Activity
import android.os.Bundle
import br.com.concrete.tentacle.R
import kotlinx.android.synthetic.main.activity_forgot_pass_send_email.*
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.*

class ForgotPassSendEmailActivity : Activity() {

    companion object {
        const val EMAIL = "EMAIL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_send_email)

        init()
    }

    private fun init() {
        if (intent.hasExtra(EMAIL)) {
            val email = intent.extras?.getString(EMAIL)
            email?.let {
                edtEmail.edt.setText(it)
            }
        }
    }

}
