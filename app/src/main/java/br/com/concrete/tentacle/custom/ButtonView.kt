package br.com.concrete.tentacle.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.concrete.tentacle.R
import kotlinx.android.synthetic.main.button_custom.view.*

class ButtonView(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.button_custom, this)
        val buttonName: String?
        val textColor: String?
        val progressColor: String?
        val typedArray: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ButtonView, 0, 0)

        try {
            buttonName = typedArray.getString(R.styleable.ButtonView_buttonName)
            textColor = typedArray.getString(R.styleable.ButtonView_textColorButton)
            progressColor = typedArray.getString(R.styleable.ButtonView_progressBarColor)
        } finally {
            typedArray.recycle()
        }

        labelCustomView(buttonName, textColor, progressColor)
    }

    private fun labelCustomView(buttonName: String?, textColor: String?, progressColor: String?) {
        button.text = buttonName
        button.setTextColor(Color.parseColor(textColor))
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