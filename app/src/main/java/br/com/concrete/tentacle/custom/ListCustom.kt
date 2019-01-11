package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.TypedArrayUtils.getResourceId
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.withStyledAttributes
import br.com.concrete.tentacle.utils.DEFAULT_INVALID_RESOURCE
import kotlinx.android.synthetic.main.list_custom.view.*

class ListCustom(
    context: Context,
    attrs: AttributeSet
): LinearLayout(context, attrs){

    private var iconReference: Int = DEFAULT_INVALID_RESOURCE
    private var errorDescriptionReference: Int = DEFAULT_INVALID_RESOURCE
    private var buttonNameReference: Int = DEFAULT_INVALID_RESOURCE

    init {
        View.inflate(context, R.layout.list_custom, this)
        context.withStyledAttributes(attrs,
            R.styleable.ListCustom,
            0,
            0
        ){
            iconReference = getResourceId(R.styleable.ListCustom_icon, DEFAULT_INVALID_RESOURCE)
            errorDescriptionReference = getResourceId(R.styleable.ListCustom_errorDescription, DEFAULT_INVALID_RESOURCE)
            buttonNameReference = getResourceId(R.styleable.ListCustom_buttonNameError, DEFAULT_INVALID_RESOURCE)
        }
    }

    fun <T> updateUi(elements: List<T>?){
        if(elements != null){
            recyclerListView.visibility = View.VISIBLE
            recyclerListError.visibility = View.GONE
        }else{
            recyclerListError.setUpComponents(iconReference, errorDescriptionReference, buttonNameReference)
            recyclerListView.visibility = View.VISIBLE
            recyclerListError.visibility = View.VISIBLE
        }
    }
}
