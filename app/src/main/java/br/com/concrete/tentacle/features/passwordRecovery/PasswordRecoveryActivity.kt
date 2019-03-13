package br.com.concrete.tentacle.features.passwordRecovery

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.inTransaction
import kotlinx.android.synthetic.main.activity_password_recovery.fragmentContainer

const val ARGUMENT_EMAIL = "email_argument"

class PasswordRecoveryActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_password_recovery)

        val email = intent.extras?.getString(ARGUMENT_EMAIL)
        val fragment = PasswordRecoveryFragment.newInstance(email)

        supportFragmentManager.inTransaction {
            add(R.id.fragmentContainer, fragment)
        }
    }

}