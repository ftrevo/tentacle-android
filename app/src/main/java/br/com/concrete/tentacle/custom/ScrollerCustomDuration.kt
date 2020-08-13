package br.com.concrete.tentacle.custom

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class ScrollerCustomDuration(
    context: Context,
    interpolator: Interpolator?
) : Scroller(context, interpolator) {

    private var mScrollFactor: Double = 1.0

    fun setScrollDurationFactor(scrollFactor: Double) {
        mScrollFactor = scrollFactor
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, (duration * mScrollFactor).toInt())
    }
}