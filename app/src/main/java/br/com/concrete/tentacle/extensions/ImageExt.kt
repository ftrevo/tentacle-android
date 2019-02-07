package br.com.concrete.tentacle.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(imageResource: Int) {
    Glide.with(this)
        .load(imageResource)
        .into(this)
}