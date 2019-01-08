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

    fun clickRegisterAccount() = actionClick(R.id.tvRegisterAccount)

    fun matchesError(message: String) = matchTextError(message)
}