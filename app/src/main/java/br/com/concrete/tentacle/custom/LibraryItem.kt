package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.concrete.tentacle.R
import kotlinx.android.synthetic.main.library_item_layout.view.ivArrow
import kotlinx.android.synthetic.main.library_item_layout.view.tv360
import kotlinx.android.synthetic.main.library_item_layout.view.tv3DS
import kotlinx.android.synthetic.main.library_item_layout.view.tvGameName
import kotlinx.android.synthetic.main.library_item_layout.view.tvONE
import kotlinx.android.synthetic.main.library_item_layout.view.tvPS3
import kotlinx.android.synthetic.main.library_item_layout.view.tvPS4
import kotlinx.android.synthetic.main.library_item_layout.view.tvNS


class LibraryItem(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private var viewStateOpen = false
    private var ctx: Context = context

    init {
        View.inflate(context, R.layout.library_item_layout, this)

        ivArrow.setOnClickListener {
            if(viewStateOpen) animateOpen() else animateClose()
            viewStateOpen = !viewStateOpen
        }

    }

    private fun animateOpen(){
        val anim = AnimationUtils.loadAnimation(ctx, R.anim.rotate_open)
        anim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {
                showBullets(true)
            }

        })

        ivArrow.startAnimation(anim)
    }

    private fun animateClose(){
        val anim = AnimationUtils.loadAnimation(ctx, R.anim.rotate_close)
        anim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {
                showBullets(false)
            }

        })

        ivArrow.startAnimation(anim)
    }

    private fun showBullets(show: Boolean){
        tvNS.visibility = if(show) View.VISIBLE else View.GONE
        tv3DS.visibility = if(show) View.VISIBLE else View.GONE
        tvONE.visibility = if(show) View.VISIBLE else View.GONE
        tv360.visibility = if(show) View.VISIBLE else View.GONE
        tvPS4.visibility = if(show) View.VISIBLE else View.GONE
        tvPS3.visibility = if(show) View.VISIBLE else View.GONE
    }
}