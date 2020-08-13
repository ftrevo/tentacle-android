package br.com.concrete.tentacle.extensions

import android.widget.RatingBar

fun RatingBar.progress(value: Float) {
    this.progress = Math.round(value) / 10
}