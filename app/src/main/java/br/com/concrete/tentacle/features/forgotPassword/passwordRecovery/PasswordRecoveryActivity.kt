package br.com.concrete.tentacle.features.forgotPassword.passwordRecovery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.inTransaction

const val ARGUMENT_EMAIL = "email_argument"

class PasswordRecoveryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_recovery)

        val email = intent.extras?.getString(ARGUMENT_EMAIL)
        val fragment = PasswordRecoveryFragment.newInstance(email)

        supportFragmentManager.inTransaction {
            add(R.id.fragmentContainer, fragment)
        }
    }
}