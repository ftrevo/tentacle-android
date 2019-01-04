package br.com.concrete.tentacle.extensions

import android.util.Patterns

fun String.validateEmail(): Boolean = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.validatePassword(): Boolean = this.isNotEmpty() && this.length > 5

fun String.digits(): String {
    return this.replace("(", "")
        .replace(")", "")
        .replace("-", "")
}