package br.com.concrete.tentacle.custom

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.withStyledAttributes
import br.com.concrete.tentacle.utils.DEFAULT_INVALID_RESOURCE
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.*

class TentacleEditText(
    context: Context,
    attrs: AttributeSet
) : BaseTentacleLabeledWidget(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.tentacle_edit_text_layout, this, true)

        context.withStyledAttributes(
            attrs,
            R.styleable.TentacleLabeledWidget,
            0,
            0) {

            val labelText = getResourceId(R.styleable.TentacleLabeledWidget_label, DEFAULT_INVALID_RESOURCE)
            val errorText = getResourceId(R.styleable.TentacleLabeledWidget_errorLabel, DEFAULT_INVALID_RESOURCE)
            val inputType = getInt(R.styleable.TentacleLabeledWidget_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL)
            val imeOptions = getInt(R.styleable.TentacleLabeledWidget_android_imeOptions, 0)
            val gravity = getInt(R.styleable.TentacleLabeledWidget_android_gravity, 0)
            val maxLenght = getInt(R.styleable.TentacleLabeledWidget_android_maxLength, -1)

            if (labelText > DEFAULT_INVALID_RESOURCE) tvLabel.text = context.getString(labelText)
            if (errorText > DEFAULT_INVALID_RESOURCE) tvError.text = context.getString(errorText)

            edt.inputType = inputType
            edt.imeOptions = imeOptions
            edt.gravity = gravity
            if (maxLenght >= 0) edt.filters = arrayOf(InputFilter.LengthFilter(maxLenght))
        }
    }

    fun getText() = edt.text.toString()

    fun setText(text: String) {
        edt.setText(text)
    }

    fun setLetterSpacing(space: Float) {
        edt.letterSpacing = space
    }
}