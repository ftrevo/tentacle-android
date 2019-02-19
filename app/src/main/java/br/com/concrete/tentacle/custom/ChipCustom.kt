package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.utils.Utils
import com.google.android.material.chip.Chip

class ChipCustom(context: Context, attrs: AttributeSet?) : Chip(context, attrs) {

    init {
        chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.color_chip_state)
        chipStrokeColor = null
        isCheckedIconVisible = false
        isCheckable = true
        rippleColor = null
        setTextColor(ContextCompat.getColor(context, R.color.textColor))
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        chipStrokeWidth = 0f
        chipCornerRadius = Utils.dpToPx(20).toFloat()
    }
}
