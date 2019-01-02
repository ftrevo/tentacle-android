package br.com.concrete.tentacle.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.withStyledAttributes
import kotlinx.android.synthetic.main.button_custom.view.*

class ButtonView(
        context: Context,
        attrs: AttributeSet
    ): ConstraintLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.button_custom, this)
        var buttonName: String?
        var textColor: String?
        var textSize: Float

        context.withStyledAttributes(
            attrs,
            R.styleable.ButtonView,
            0,
            0
        ){
            buttonName = getString(R.styleable.ButtonView_buttonName)
            textColor = getString(R.styleable.ButtonView_textColorButton)
            textSize = getFloat(R.styleable.ButtonView_textSizeButton, context.resources.getDimension(R.dimen.defaultTextSize))

            labelCustomView(buttonName, textColor, textSize)
        }
    }

    private fun labelCustomView(
        buttonName: String?,
        textColor: String?,
        textSize: Float
    ) {
        buttonName?.let {
            button.text = if (it.isNotEmpty()) it else context.getString(R.string.button)
        }

        button.textSize = textSize

        textColor?.let{
            val color: Int = if (it.isNotEmpty()) Color.parseColor(it) else Color.GRAY
            button.setTextColor(color)
        }
    }

    fun isLoading(isLoading: Boolean) {
        visibilityProgress(isLoading)
    }

    private fun visibilityProgress(condition: Boolean) {
        if (condition) {
            progressBar.visibility = View.VISIBLE
            button.visibility = View.GONE
            frameLayout.isEnabled = !condition
        } else {
            progressBar.visibility = View.GONE
            button.visibility = View.VISIBLE
            frameLayout.isEnabled = !condition
        }
    }
}