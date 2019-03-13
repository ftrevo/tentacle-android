package br.com.concrete.tentacle.features.passwordRecovery

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.extensions.callSnackbar
import br.com.concrete.tentacle.extensions.validadeToken
import br.com.concrete.tentacle.extensions.validateEmail
import br.com.concrete.tentacle.extensions.validatePassword
import br.com.concrete.tentacle.utils.EMPTY_STRING
import kotlinx.android.synthetic.main.fragment_password_recovery.emailEditText
import kotlinx.android.synthetic.main.fragment_password_recovery.newPassConfirmationEditText
import kotlinx.android.synthetic.main.fragment_password_recovery.newPassEditText
import kotlinx.android.synthetic.main.fragment_password_recovery.recoveryPasswordButton
import kotlinx.android.synthetic.main.fragment_password_recovery.tokenEditText
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.edt

class PasswordRecoveryFragment: BaseFragment() {

    companion object {

        fun newInstance(emailArgument: String?): PasswordRecoveryFragment {
            val fragment = PasswordRecoveryFragment()

            emailArgument?.let {
                val args = Bundle()
                args.putString(ARGUMENT_EMAIL, it)
                fragment.arguments = args
            }

            return fragment
        }
    }

    override fun getToolbarTitle(): Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_password_recovery, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareViews()
        setListeners()
    }

    private fun prepareViews() {
        with(tokenEditText) {
            this.setLetterSpacing(3f)
        }

        arguments?.let {
            val email = it.getString(ARGUMENT_EMAIL, EMPTY_STRING)
            emailEditText.edt.setText(email)
        }
    }

    private fun setListeners() {
        recoveryPasswordButton.setOnClickListener {
            handleConfirmationClick()
        }

        emailEditText.edt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }

        tokenEditText.edt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateToken()
            }
        }
        newPassEditText.edt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword()
            }
        }

        newPassConfirmationEditText.edt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePasswordConfirmation()
            }
        }
    }

    private fun validateEmail() {
        val email = emailEditText.edt.text.toString()
        if (email.isNotEmpty()) {
            emailEditText.showError(!email.validateEmail())
        } else {
            emailEditText.showError(false)
        }
    }

    private fun validateToken() {
        val token = tokenEditText.edt.text.trim().toString()
        if (token.isNotEmpty()) {
            tokenEditText.showError(!token.validadeToken())
        } else {
            tokenEditText.showError(false)
        }
    }

    private fun validatePassword() {
        val password = newPassEditText.edt.text.trim().toString()
        if (password.isNotEmpty()) {
            newPassEditText.showError(!password.validatePassword())
        } else {
            newPassEditText.showError(false)
        }
    }

    private fun validatePasswordConfirmation() {
        val confirmPass = newPassConfirmationEditText.edt.text.trim().toString()
        val pass = newPassEditText.edt.text.trim().toString()

        if (confirmPass.isNotEmpty()) {
            if (!confirmPass.validatePassword()) {
                newPassConfirmationEditText.setError(getString(R.string.password_error))
                newPassConfirmationEditText.showError(true)
            } else if (!pass.equals(confirmPass)) {
                newPassConfirmationEditText.setError(getString(R.string.password_confirmation_error))
                newPassConfirmationEditText.showError(true)
            } else {
                newPassConfirmationEditText.showError(false)
            }
        } else {
            newPassConfirmationEditText.showError(false)
        }
    }

    private fun handleConfirmationClick() {

        val email = emailEditText.getText()
        val token = tokenEditText.getText()

        val newPassword = newPassEditText.getText()
        val newPasswordConfirmation = newPassConfirmationEditText.getText()

        var isOk = true

        if (!email.validateEmail()) {
            isOk = false
            emailEditText.showError(true)
        }

        if (!token.validadeToken()) {
            isOk = false
            tokenEditText.showError(true)
        }

        if (!newPassword.validatePassword()) {
            isOk = false
            newPassEditText.showError(true)
        } else {
            if (!newPasswordConfirmation.validatePassword()) {
                isOk = false
                newPassConfirmationEditText.setError(getString(R.string.password_error))
                newPassConfirmationEditText.showError(true)
            } else if (!newPassword.equals(newPasswordConfirmation)) {
                isOk = false
                newPassConfirmationEditText.setError(getString(R.string.password_confirmation_error))
                newPassConfirmationEditText.showError(true)
            }
        }

        when (isOk) {
            true -> {}//TODO - confirm new password
            false -> context?.callSnackbar(view!!, getString(R.string.verificar_campos_login))
        }
    }


}