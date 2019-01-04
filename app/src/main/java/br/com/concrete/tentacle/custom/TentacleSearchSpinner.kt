package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.withStyledAttributes
import kotlinx.android.synthetic.main.tentacle_search_spinner_layout.view.*

class TentacleSearchSpinner(context: Context, attrs: AttributeSet) : BaseTentacleLabeledWidget(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.tentacle_search_spinner_layout, this, true)

        context.withStyledAttributes(
            attrs,
            R.styleable.TentacleLabeledWidget,
            0,
            0) {
            val labelText = getResourceId(R.styleable.TentacleLabeledWidget_label, -1)
            val errorText = getResourceId(R.styleable.TentacleLabeledWidget_errorLabel, -1)

            if (labelText > -1) tvLabel.text = context.getString(labelText)
            if (errorText > -1) tvError.text = context.getString(errorText)
        }

        showError(false)
    }

    fun setText(text: String) {
        button.text = text
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        button.setOnClickListener(listener)
    }
}