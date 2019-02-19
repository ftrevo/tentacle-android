package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import br.com.concrete.tentacle.R
import kotlinx.android.synthetic.main.button_custom.view.button

class StateButtomView(
    context: Context,
    attrs: AttributeSet
) : ButtonView(context, attrs) {

    fun enable() {
        isEnabled = true
        backgroundColor(R.drawable.shape_border_rounded_blue)
        textColor(R.color.textColor)
    }

    fun disable() {
        isEnabled = false
        backgroundColor(R.drawable.shape_border_rounded_diasabled)
        textColor(R.color.disableTextButton)
    }

    private fun backgroundColor(@DrawableRes drawable: Int) {
        background = ContextCompat.getDrawable(context, drawable)
    }

    private fun textColor(@ColorRes color: Int) {
        button.setTextColor(ContextCompat.getColor(context, color))
    }
}