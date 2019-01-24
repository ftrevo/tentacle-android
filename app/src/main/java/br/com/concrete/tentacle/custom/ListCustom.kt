package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.withStyledAttributes
import br.com.concrete.tentacle.utils.DEFAULT_INVALID_RESOURCE
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.buttonAction
import kotlinx.android.synthetic.main.list_custom.view.progressBarList
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError

class ListCustom(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private var iconReference: Int = DEFAULT_INVALID_RESOURCE
    private var errorDescriptionReference: Int = DEFAULT_INVALID_RESOURCE
    private var buttonNameErrorReference: Int = DEFAULT_INVALID_RESOURCE
    private var buttonNameActionReference: Int = DEFAULT_INVALID_RESOURCE

    init {
        View.inflate(context, R.layout.list_custom, this)
        context.withStyledAttributes(attrs,
            R.styleable.ListCustom,
            0,
            0
        ) {
            setLoading(true)
            iconReference = getResourceId(R.styleable.ListCustom_icon, DEFAULT_INVALID_RESOURCE)
            errorDescriptionReference = getResourceId(R.styleable.ListCustom_errorDescription, DEFAULT_INVALID_RESOURCE)
            buttonNameErrorReference = getResourceId(R.styleable.ListCustom_buttonNameError, DEFAULT_INVALID_RESOURCE)
            buttonNameActionReference = getResourceId(R.styleable.ListCustom_buttonNameAction, DEFAULT_INVALID_RESOURCE)
        }
    }

    fun <T> updateUi(elements: ArrayList<T>?) {
        if(elements == null){
            recyclerListError.setUpComponents(iconReference, errorDescriptionReference, buttonNameErrorReference)
            showError()
        }else{
            if(elements.isEmpty()) {
                recyclerListError.setUpComponents(iconReference, errorDescriptionReference, buttonNameActionReference)
                showError()
            }else{
                recyclerListView.visibility = View.VISIBLE
                recyclerListError.visibility = View.GONE
                if (buttonNameActionReference > DEFAULT_INVALID_RESOURCE) {
                    buttonAction.setButtonName(context.getString(buttonNameActionReference))
                    buttonAction.visibility = View.VISIBLE
                    recyclerListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            when (newState) {
                                View.SCROLL_INDICATOR_BOTTOM -> {
                                    buttonAction.visibility = View.GONE
                                }
                                View.SCROLL_INDICATOR_TOP -> {
                                    buttonAction.visibility = View.GONE
                                }
                                View.SCROLL_AXIS_NONE -> {
                                    buttonAction.visibility = View.VISIBLE
                                }
                            }
                            super.onScrollStateChanged(recyclerView, newState)
                        }
                    })
                }
            }
        }
    }

    private fun showError(){
        recyclerListView.visibility = View.GONE
        buttonAction.visibility = View.GONE
        recyclerListError.visibility = View.VISIBLE
    }

    fun setLoading(condition: Boolean) {
        if (condition) {
            progressBarList.visibility = View.VISIBLE
        } else {
            progressBarList.visibility = View.GONE
        }
    }

    fun setErrorMessage(errorCode: Int) {
        errorDescriptionReference = errorCode
    }

    fun setButtonText(errorCode: Int) {
        errorDescriptionReference = errorCode
    }

    fun setButtonAction(action: () -> Unit) {
        buttonAction.setOnClickListener { action() }
    }
}
