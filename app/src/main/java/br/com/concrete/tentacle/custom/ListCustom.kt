package br.com.concrete.tentacle.custom

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.hideKeyboard
import br.com.concrete.tentacle.extensions.withStyledAttributes
import br.com.concrete.tentacle.utils.DEFAULT_INVALID_RESOURCE
import br.com.concrete.tentacle.utils.DEFAULT_INVALID_RESOURCE_BOOLEAN
import kotlinx.android.synthetic.main.list_custom.view.buttonAction
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import kotlinx.android.synthetic.main.progress_include.view.progressBarList

class ListCustom(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private var iconReference: Int = DEFAULT_INVALID_RESOURCE
    private var errorDescriptionReference: Int = DEFAULT_INVALID_RESOURCE
    private var buttonNameErrorReference: Int = DEFAULT_INVALID_RESOURCE
    private var buttonNameActionReference: Int = DEFAULT_INVALID_RESOURCE
    private var endLessScroll: Boolean = DEFAULT_INVALID_RESOURCE_BOOLEAN

    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    private lateinit var mOnScrollListener: OnScrollListener
    private var emptyListMessageReference: Int = DEFAULT_INVALID_RESOURCE

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
            emptyListMessageReference = getResourceId(R.styleable.ListCustom_emptyListMessage, DEFAULT_INVALID_RESOURCE)
            endLessScroll = getBoolean(R.styleable.ListCustom_endLessScroll, DEFAULT_INVALID_RESOURCE_BOOLEAN)
        }
    }

    fun <T> updateUi(elements: ArrayList<T>?, filtering: Boolean = false) {
        if (elements == null) {
            recyclerListError.setUpComponents(iconReference, errorDescriptionReference, buttonNameErrorReference)
            showViewError()
        } else {
            if (elements.isEmpty()) {
                val msg = if (filtering) emptyListMessageReference else errorDescriptionReference
                recyclerListError.setUpComponents(iconReference, msg, buttonNameErrorReference)
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
        initEndLessRecyclerView()
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
                        } else if (layoutManager.findLastCompletelyVisibleItemPosition() == mOnScrollListener.count()) {
                            buttonAction.visibility = View.VISIBLE
                        }
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
    }

    private fun initEndLessRecyclerView() {
        recyclerListView.adapter?.let {
            recyclerListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (endLessScroll) {
                        val layoutManager = recyclerView.layoutManager
                        if (layoutManager is LinearLayoutManager) {
                            visibleItemCount = layoutManager.childCount
                            totalItemCount = layoutManager.itemCount
                            pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition()
                        }

                        if (mOnScrollListener.loadPage() && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            if (mOnScrollListener.count() > mOnScrollListener.sizeElements()) {
                                mOnScrollListener.loadMore()
                                buttonAction.visibility = View.GONE
                            }
                        }
                    }
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
            if (it is Activity) {
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

    fun setOnScrollListener(onScrollListener: OnScrollListener) {
        this.mOnScrollListener = onScrollListener
    }

    interface OnScrollListener {
        fun count(): Int
        fun sizeElements(): Int
        fun loadMore()
        fun loadPage(): Boolean
    }
}