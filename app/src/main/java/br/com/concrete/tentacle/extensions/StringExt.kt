package br.com.concrete.tentacle.extensions

import android.util.Patterns

fun String.validateEmail(): Boolean = !Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.validatePassword(): Boolean = (this.length < 5)