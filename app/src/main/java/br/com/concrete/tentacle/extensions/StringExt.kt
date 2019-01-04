package br.com.concrete.tentacle.extensions

import android.util.Patterns

fun String.validateEmail() = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.validatePassword() = this.isNotEmpty() && this.length > 5

fun String.digits() = this.replace("(", "")
    .replace(")", "")
    .replace("-","")