package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Interpolator
import androidx.viewpager.widget.ViewPager
import java.lang.reflect.Field

class NoSwipeViewPager(context: Context, attributeSet: AttributeSet) : ViewPager(context, attributeSet) {

    private var mScroller : ScrollerCustomDuration? = null

    init {
        try {
            val scroller = ViewPager::class.java.getDeclaredField("mScroller")
            scroller.isAccessible = true
            val interpolator : Field = ViewPager::class.java.getDeclaredField("sInterpolator")
            interpolator.isAccessible = true

            mScroller =  ScrollerCustomDuration(context, interpolator.get(null) as Interpolator)
            scroller.set(this, mScroller)

        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    fun setScrollDurationFactor(scrollFactor: Double){
        mScroller?.setScrollDurationFactor(scrollFactor)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    fun nextPage() {
        currentItem += 1
    }
}