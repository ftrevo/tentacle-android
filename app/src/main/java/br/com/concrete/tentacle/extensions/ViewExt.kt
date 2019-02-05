package br.com.concrete.tentacle.extensions

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

fun View.animation(@AnimRes animation: Int, animStart: () -> Unit ) {
    val anim = AnimationUtils.loadAnimation(this.context, animation)
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
        }

        override fun onAnimationStart(animation: Animation?) {
            animStart()
        }
    })

    this.startAnimation(anim)
}