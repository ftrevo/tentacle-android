package br.com.concrete.tentacle.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus
    view?.let {
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Activity.showKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus
    view?.let {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}