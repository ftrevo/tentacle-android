package br.com.concrete.tentacle.ui.login

import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseTestRobot

fun login(func: LoginRobot.()-> Unit) = LoginRobot().apply {
    func()
}

class LoginRobot: BaseTestRobot() {

    fun setEmail(email: String) = fillEditText(R.id.edtEmail, email)

    fun setPassword(password: String) = fillEditText(R.id.edtPassword, password)

    fun clickButtonLogin() = actionClick(R.id.btLogin)

    fun clickRegisterAcc() = actionClick(R.id.tvRegisterAccount)

    fun matchesError(message: String) = matchTextError(message)

    fun matchesNotError(message: String) = matchWithoutTextError(message)

    fun matchesNotErrorSnackBar(message: String) = matchWithoutSnackBar(message)

    fun matchDisplayedMessageOla() = matchDisplayed(R.id.tvOla)

    fun matchDisplayedMessage() = matchDisplayed(R.id.tvMessage)

    fun matchDisplayedTentacleEditTextEmail() = matchDisplayed(R.id.edtEmail)

    fun matchDisplayedTentacleEditTextPassword() = matchDisplayed(R.id.edtPassword)

    fun matchDisplayedButtonLogin() = matchDisplayed(R.id.btLogin)

    fun matchDisplayedTextRegisterAccount() = matchDisplayed(R.id.tvRegisterAccount)
}