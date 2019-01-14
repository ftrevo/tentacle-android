package br.com.concrete.tentacle.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.withStyledAttributes
import kotlinx.android.synthetic.main.icon_bottom_bar.view.*

class IconBottomBar(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    private var isViewSelected = false

    init {
        View.inflate(context, R.layout.icon_bottom_bar, this)

        context.withStyledAttributes(
                attrs,
                R.styleable.IconBottomBar,
                0,
                0
        ) {
            val icon = getDrawable(R.styleable.IconBottomBar_icon)
            val selected = getBoolean(R.styleable.IconBottomBar_selected, false)

            setup(icon, selected)
        }
        setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                setViewSelected(!isViewSelected)
            }
            return@setOnTouchListener false
        }
    }

    private fun setup(icon: Drawable?, selected: Boolean) {
        icon?.let {
            ivIcon.setImageDrawable(it)
        }
        setViewSelected(selected)
    }

    fun setViewSelected(selected: Boolean) {
        selectedView.visibility = if (selected) View.VISIBLE else View.INVISIBLE
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, if (selected) R.color.activateIcon else R.color.deactivateIcon))
        ImageViewCompat.setImageTintList(ivIcon, colorStateList)

        isViewSelected = selected
    }
}