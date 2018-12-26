package br.com.concrete.tentacle.extensions

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun Context.callSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}