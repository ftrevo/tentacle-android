package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.asSequence
import br.com.concrete.tentacle.extensions.withStyledAttributes
import br.com.concrete.tentacle.utils.DEFAULT_INVALID_RESOURCE
import kotlinx.android.synthetic.main.bottom_bar.view.*

class BottomBar(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {

    private var iconBottomBarSelected = R.id.action_home

    init {
        View.inflate(context, R.layout.bottom_bar, this)

        context.withStyledAttributes(
                attrs,
                R.styleable.BottomBar,
                0,
                0
        ) {
            val defaultSelectedPosition = getInt(R.styleable.BottomBar_defaultSelectedPosition, DEFAULT_INVALID_RESOURCE)
            if (defaultSelectedPosition != DEFAULT_INVALID_RESOURCE) {
                updateBottomBarByPosition(defaultSelectedPosition)
            }
        }
    }

    fun startListener(listener: (Int) -> Unit) {
        container.asSequence().toList().map { item ->
            item.setOnClickListener {
                if (item.id != iconBottomBarSelected) {
                    updateBottomBar(item.id)
                    listener(item.id)
                }
                iconBottomBarSelected = item.id
            }
        }
    }

    fun updateBottomBar(id: Int) {
        container.asSequence().toList().map { item ->
            if (item is IconBottomBar) {
                item.setViewSelected(id == item.id)
            }
        }
    }

    private fun updateBottomBarByPosition(position: Int) {
        val item = container.asSequence().toList()[position]
        if (item is IconBottomBar) item.setViewSelected(true)
    }
}