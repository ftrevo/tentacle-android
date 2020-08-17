package br.com.concrete.tentacle.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import br.com.concrete.tentacle.R
import com.google.android.material.snackbar.Snackbar

fun Context.callSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

fun Context.withStyledAttributes(
    set: AttributeSet? = null,
    attrs: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
    block: TypedArray.() -> Unit
) {
    val typedArray = obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes)
    try {
        typedArray.block()
    } finally {
        typedArray.recycle()
    }
}

fun Context.getFloatFromRes(resource: Int): Float {
    val typedValue = TypedValue()
    resources.getValue(R.dimen.defaultTextSize, typedValue, true)
    return typedValue.float
}

inline fun <reified T: Activity> Context.startActivityWithoutAnimation(
    extras: Bundle? = null
){
    val intent = Intent(this, T::class.java)
    extras?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
}