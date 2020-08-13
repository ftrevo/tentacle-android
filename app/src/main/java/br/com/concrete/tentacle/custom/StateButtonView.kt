package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import br.com.concrete.tentacle.R
import kotlinx.android.synthetic.main.button_custom.view.button

open class StateButtonView(
    context: Context,
    attrs: AttributeSet
) : ButtonView(context, attrs) {

    open fun enable() {
        isEnabled = true
        backgroundColor(R.drawable.shape_border_rounded_blue)
        textColor(R.color.textColor)
    }

    open fun disable() {
        isEnabled = false
        backgroundColor(R.drawable.shape_border_rounded_diasabled)
        textColor(R.color.disableTextButton)
    }

    protected fun backgroundColor(@DrawableRes drawable: Int) {
        background = ContextCompat.getDrawable(context, drawable)
    }

    protected fun textColor(@ColorRes color: Int) {
        button.setTextColor(ContextCompat.getColor(context, color))
    }
}