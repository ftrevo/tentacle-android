package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import br.com.concrete.tentacle.R

class StateButtonWhiteView(
    context: Context,
    attrs: AttributeSet
) : StateButtonView(context, attrs) {

    override fun enable() {
        isEnabled = true
        backgroundColor(R.drawable.shape_border_rounded_white)
        textColor(R.color.colorAccent)
    }
}