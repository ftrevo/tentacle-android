package br.com.concrete.tentacle.extensions

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
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
){
    val typedArray = obtainStyledAttributes(set, attrs,defStyleAttr, defStyleRes)
    try{
        typedArray.block()
    }finally {
        typedArray.recycle()
    }
}