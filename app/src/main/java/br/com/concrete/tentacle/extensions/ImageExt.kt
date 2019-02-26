package br.com.concrete.tentacle.extensions

import android.widget.ImageView
import br.com.concrete.tentacle.di.GlideApp
import com.bumptech.glide.Glide

fun ImageView.loadImage(imageResource: Int) {
    GlideApp.with(this)
        .load(imageResource)
        .into(this)
}

fun ImageView.loadImageUrl(imageUrl: String) {
    GlideApp.with(this)
        .load(imageUrl)
        .centerCrop()
        .into(this)
}

fun ImageView.loadRoundImageUrl(imageUrl: String) {
    GlideApp.with(this)
        .load(imageUrl)
        .circleCrop()
        .into(this)
}