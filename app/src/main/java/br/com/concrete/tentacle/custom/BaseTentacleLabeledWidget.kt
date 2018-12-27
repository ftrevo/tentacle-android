package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.*

abstract class BaseTentacleLabeledWidget(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {


    fun showError(show: Boolean){
        tvError.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    fun getEditText(): TextInputEditText{
        return edt
    }

    fun getLabel(): TextView{
        return tvLabel
    }

    fun getErrorLabel(): TextView{
        return tvError
    }
}