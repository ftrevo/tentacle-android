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

class ListCustom(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private var iconReference: Int = DEFAULT_INVALID_RESOURCE
    private var errorDescriptionReference: Int = DEFAULT_INVALID_RESOURCE
    private var buttonNameReference: Int = DEFAULT_INVALID_RESOURCE
    private var buttonNameActionReference: Int = DEFAULT_INVALID_RESOURCE

    init {
        View.inflate(context, R.layout.list_custom, this)
        context.withStyledAttributes(
            attrs,
            R.styleable.ListCustom,
            0,
            0
        ) {
            setLoading(true)
            iconReference = getResourceId(R.styleable.ListCustom_icon, DEFAULT_INVALID_RESOURCE)
            errorDescriptionReference = getResourceId(R.styleable.ListCustom_errorDescription, DEFAULT_INVALID_RESOURCE)
            buttonNameReference = getResourceId(R.styleable.ListCustom_buttonNameError, DEFAULT_INVALID_RESOURCE)
            buttonNameActionReference = getResourceId(R.styleable.ListCustom_buttonNameAction, DEFAULT_INVALID_RESOURCE)
        }
    }

    fun <T> updateUi(elements: ArrayList<T>?) {
        if (elements != null && !elements.isEmpty()) {
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
        } else {
            recyclerListError.setUpComponents(iconReference, errorDescriptionReference, buttonNameReference)
            recyclerListView.visibility = View.GONE
            buttonAction.visibility = View.GONE
            recyclerListError.visibility = View.VISIBLE
        }
    }

    fun setLoading(condition: Boolean) {
        progressBarList.visibility = if (condition) View.VISIBLE else View.GONE
    }

    fun visibleCustomError(visible: Boolean) {
        recyclerListError.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
