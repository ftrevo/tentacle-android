package br.com.concrete.tentacle.extensions

import android.util.Patterns

fun String.validaEmail(email: String): Boolean = !Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun String.validaPassword(password: String): Boolean = (password.length < 5)