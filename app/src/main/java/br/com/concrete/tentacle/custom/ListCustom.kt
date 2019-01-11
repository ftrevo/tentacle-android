package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R

class ListCustom<T>(
    val list: List<T>,
    context: Context,
    attributeSet: AttributeSet
): RecyclerView(context, attributeSet){

    init {
        View.inflate(context, R.layout.list_error_custom, this)


    }

}
