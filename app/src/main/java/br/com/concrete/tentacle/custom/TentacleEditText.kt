package br.com.concrete.tentacle.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import br.com.concrete.tentacle.R
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.*

class TentacleEditText(context: Context, attrs: AttributeSet): BaseTentacleLabeledWidget(context, attrs) {

    init{
        LayoutInflater.from(context).inflate(R.layout.tentacle_edit_text_layout, this, true)

        var typedArray: TypedArray? = null
        try{
            typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TentacleLabeledWidget, 0, 0)
            val labelText = typedArray.getResourceId(R.styleable.TentacleLabeledWidget_label, -1)
            val errorText = typedArray.getResourceId(R.styleable.TentacleLabeledWidget_errorLabel, -1)
            val inputType = typedArray.getInt(R.styleable.TentacleLabeledWidget_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL)
            val imeOptions = typedArray.getInt(R.styleable.TentacleLabeledWidget_android_imeOptions, 0)

            if(labelText > -1)  tvLabel.text = context.getString(labelText)
            if(errorText > -1) tvError.text = context.getString(errorText)

            edt.inputType = inputType
            edt.imeOptions = imeOptions

        }finally {
            typedArray?.recycle()
        }


        showError(false)
    }

    fun getText(): String{
        return edt.text.toString()
    }
}