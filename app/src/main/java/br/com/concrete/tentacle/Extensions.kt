package br.com.concrete.tentacle

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.resError(error: Int){
    this.error = this.context.getString(error)
}

