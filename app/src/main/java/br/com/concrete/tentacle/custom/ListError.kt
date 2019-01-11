package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.withStyledAttributes
import br.com.concrete.tentacle.utils.DEFAULT_INVALID_RESOURCE
import kotlinx.android.synthetic.main.list_error_custom.view.icon
import kotlinx.android.synthetic.main.list_error_custom.view.errorDescription
import kotlinx.android.synthetic.main.list_error_custom.view.buttonName

class ListError(
    context: Context,
    attrs: AttributeSet
): ConstraintLayout(context, attrs){

    init {
        View.inflate(context, R.layout.list_error_custom, this)

        context.withStyledAttributes(
            attrs,
            R.styleable.ButtonView,
            0,
            0
        ) {
            val iconReference = getResourceId(R.styleable.ListError_icon, DEFAULT_INVALID_RESOURCE)
            val errorDescriptionReference = getResourceId(R.styleable.ListError_textError, DEFAULT_INVALID_RESOURCE)
            val buttonNameReference = getResourceId(R.styleable.ListError_buttonName, DEFAULT_INVALID_RESOURCE)

            setUpComponents(iconReference, errorDescriptionReference, buttonNameReference)
        }
    }

    fun setUpComponents(iconReference: Int, errorDescriptionReference: Int, buttonNameReference: Int){
        if (iconReference > DEFAULT_INVALID_RESOURCE) icon.setImageResource(iconReference)
        if (errorDescriptionReference > DEFAULT_INVALID_RESOURCE) errorDescription.text = context.getString(errorDescriptionReference)
        if (buttonNameReference > DEFAULT_INVALID_RESOURCE) buttonName.setButtonName(context.getString(buttonNameReference))
    }

}