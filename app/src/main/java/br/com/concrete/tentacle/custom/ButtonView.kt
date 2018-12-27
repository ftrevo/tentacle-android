package br.com.concrete.tentacle.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.concrete.tentacle.R
import kotlinx.android.synthetic.main.button_custom.view.*

class ButtonView(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.button_custom, this)
        val buttonName: String?
        val textColor: String?
        val progressColor: String?
        val textSize: Float
        val typedArray: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ButtonView, 0, 0)

        try {
            buttonName = typedArray.getString(R.styleable.ButtonView_buttonName)
            textColor = typedArray.getString(R.styleable.ButtonView_textColorButton)
            progressColor = typedArray.getString(R.styleable.ButtonView_progressBarColor)
            textSize = typedArray.getFloat(R.styleable.ButtonView_textSizeButton, 14F)
        } finally {
            typedArray.recycle()
        }

        labelCustomView(buttonName, textColor, progressColor, textSize)
    }

    private fun labelCustomView(
        buttonName: String?,
        textColor: String?,
        progressColor: String?,
        textSize: Float
    ) {
        button.text = if (buttonName!!.isNotEmpty()) buttonName else context.getString(R.string.button)
        button.textSize = textSize
        val color: Int = if (textColor!!.isNotEmpty()) Color.parseColor(textColor) else Color.GRAY
        button.setTextColor(color)

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