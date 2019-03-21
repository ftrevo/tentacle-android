package br.com.concrete.tentacle.extensions

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import br.com.concrete.tentacle.di.GlideApp

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

fun ImageView.loadRoundImageResource(@DrawableRes res: Int) {
    GlideApp.with(this)
        .load(res)
        .circleCrop()
        .into(this)
}

fun ImageView.loadRoundImageBitmap(bmp: Bitmap) {
    GlideApp.with(this)
        .load(bmp)
        .circleCrop()
        .into(this)
}