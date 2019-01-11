package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.withStyledAttributes
import kotlinx.android.synthetic.main.list_custom.view.*

class ListCustom<T>(
    val list: List<T>?,
    context: Context,
    attributeSet: AttributeSet
): RecyclerView(context, attributeSet){

    init {
        View.inflate(context, R.layout.list_error_custom, this)
        context.withStyledAttributes(attributeSet,
            R.styleable.ListCustom,
            0,
            0
        ){
            if(list != null && list.isEmpty()){
                recyclerListView.visibility = View.GONE
                recyclerListError.visibility = View.VISIBLE
            }else{
                recyclerListView.visibility = View.VISIBLE
                recyclerListError.visibility = View.GONE
            }
        }
    }
}
