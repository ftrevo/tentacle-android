package br.com.concrete.tentacle.custom

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.hideKeyboard
import br.com.concrete.tentacle.extensions.withStyledAttributes
import br.com.concrete.tentacle.utils.DEFAULT_INVALID_RESOURCE
import kotlinx.android.synthetic.main.list_custom.view.buttonAction
import kotlinx.android.synthetic.main.list_custom.view.progressBarList
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
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
        context.withStyledAttributes(
            attrs,
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
        if (elements == null) {
            recyclerListError.setUpComponents(iconReference, errorDescriptionReference, buttonNameErrorReference)
            showViewError()
        } else {
            if (elements.isEmpty()) {
                recyclerListError.setUpComponents(iconReference, errorDescriptionReference, buttonNameActionReference)
                showViewError()
            } else {
                showViewSuccess()
            }
        }
    }

    private fun showViewSuccess() {
        recyclerListView.visibility = View.VISIBLE
        recyclerListError.visibility = View.GONE

        if (buttonNameActionReference > DEFAULT_INVALID_RESOURCE) {
            buttonAction.setButtonName(context.getString(buttonNameActionReference))
            buttonAction.visibility = View.VISIBLE
            setButtonEffect()
        }
    }

    private fun setButtonEffect() {
        recyclerListView.adapter?.let {
            recyclerListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    val layoutManager = recyclerListView.layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        if (layoutManager.findLastCompletelyVisibleItemPosition() < it.itemCount - 1) {
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
                        } else if (layoutManager.findLastCompletelyVisibleItemPosition() == it.itemCount - 1) {
                            buttonAction.visibility = View.VISIBLE
                        }
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
    }

    private fun showViewError() {
        recyclerListView.visibility = View.GONE
        buttonAction.visibility = View.GONE
        recyclerListError.visibility = View.VISIBLE
        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        context?.let {
            if(it is Activity){
                it.hideKeyboard()
            }
        }
    }

    private fun showLoading() {
        recyclerListError.visibility = View.GONE
        recyclerListView.visibility = View.GONE
        progressBarList.visibility = View.VISIBLE
    }

    fun setLoading(condition: Boolean) {
        progressBarList.visibility = if (condition) View.VISIBLE else View.GONE
    }

    fun visibleCustomError(visible: Boolean) {
        recyclerListError.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setErrorMessage(errorCode: Int) {
        errorDescriptionReference = errorCode
    }

    fun setButtonNameAction(action: Int) {
        buttonNameActionReference = action
    }

    fun setButtonTextError(errorCode: Int) {
        buttonNameErrorReference = errorCode
    }

    fun setActionError(action: () -> Unit) {
        recyclerListError.buttonNameError.setOnClickListener {
            showLoading()
            action()
        }
    }
}
