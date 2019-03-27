package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_SCREENSHOT_HUGE
import br.com.concrete.tentacle.utils.Utils

class AnimatedImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs), Animation.AnimationListener {

    private var imageList = listOf<String>()
    private var fadeInDuration = 500L
    private var timeBetween = 3000L
    private var fadeOutDuration = 1000L
    private var imageIndex = 0

    private val fadeIn: Animation = AlphaAnimation(0f, 1f)
    private val fadeOut: Animation = AlphaAnimation(1f, 0f)
    private val animation = AnimationSet(false)
    private val forever = true

    private fun startAnimation() {
        loadImageUrl(
            Utils.assembleGameImageUrl(
                sizeType = IMAGE_SIZE_TYPE_SCREENSHOT_HUGE,
                imageId = imageList[imageIndex]
            )
        )
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = fadeInDuration

        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.startOffset = fadeInDuration + timeBetween
        fadeOut.duration = fadeOutDuration

        animation.addAnimation(fadeIn)
        animation.addAnimation(fadeOut)
        animation.repeatCount = 1
        animation.setAnimationListener(this)
        setAnimation(animation)
    }

    fun setImages(images: List<String>){
        imageList = images
        startAnimation()
    }

    override fun onAnimationEnd(animation: Animation?) {
        if(imageList.size -1 > imageIndex){
            imageIndex++
            startAnimation()
        }else{
            if(forever){
                imageIndex = 0
                startAnimation()
            }
        }
    }

    override fun onAnimationStart(animation: Animation?) {

    }

    override fun onAnimationRepeat(animation: Animation?) {

    }
}