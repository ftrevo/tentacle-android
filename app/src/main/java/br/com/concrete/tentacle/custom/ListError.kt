package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.withStyledAttributes
import br.com.concrete.tentacle.utils.DEFAULT_INVALID_RESOURCE
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import kotlinx.android.synthetic.main.list_error_custom.view.errorDescription
import kotlinx.android.synthetic.main.list_error_custom.view.icon

class ListError(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private var iconReferenceError: Int = DEFAULT_INVALID_RESOURCE
    private var errorDescriptionTextReference: Int = DEFAULT_INVALID_RESOURCE
    private var buttonNameTextErrorReference: Int = DEFAULT_INVALID_RESOURCE

    init {
        View.inflate(context, R.layout.list_error_custom, this)
        context.withStyledAttributes(
            attrs,
            R.styleable.ListError,
            0,
            0
        ) {
            iconReferenceError = getResourceId(R.styleable.ListError_iconError, DEFAULT_INVALID_RESOURCE)
            errorDescriptionTextReference = getResourceId(R.styleable.ListError_errorTextDescription, DEFAULT_INVALID_RESOURCE)
            buttonNameTextErrorReference = getResourceId(R.styleable.ListError_buttonNameTextError, DEFAULT_INVALID_RESOURCE)
        }
    }

    fun setUpComponents(iconReference: Int, errorDescriptionReference: Int, buttonNameReference: Int) {
        if (iconReference > DEFAULT_INVALID_RESOURCE) icon.setImageResource(iconReference)
        if (errorDescriptionReference > DEFAULT_INVALID_RESOURCE) errorDescription.text = context.getString(errorDescriptionReference)
        if (buttonNameReference > DEFAULT_INVALID_RESOURCE) buttonNameError.setButtonName(context.getString(buttonNameReference))
    }

    fun setUpActionErrorButton(error: () -> Unit){
        buttonNameError.setOnClickListener { error() }
    }
}