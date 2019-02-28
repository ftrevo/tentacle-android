package br.com.concrete.tentacle.features.loadmygames

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.extensions.visible
import kotlinx.android.synthetic.main.item_game.view.*

class LoadMyGamesViewHolder(
    private val mLinearLayout: View
) : RecyclerView.ViewHolder(mLinearLayout) {

    companion object {
        fun callBack(holder: RecyclerView.ViewHolder, el: Media, listener: (Media) -> Unit) {
            if (holder is LoadMyGamesViewHolder) {
                holder.mLinearLayout.game_name.text = el.game?.name ?: ""
                holder.mLinearLayout.game_media.text = el.platform.platformName
                el.activeLoan?.let {
                    holder.mLinearLayout.ivLoanRequested.visibility = View.VISIBLE
                    val color = if (it.loanDate == null) { // Loan was requested
                        ContextCompat.getColor(holder.mLinearLayout.context, R.color.colorAccent)
                    } else {
                        ContextCompat.getColor(holder.mLinearLayout.context, R.color.loanPerformed)
                    }
                    holder.mLinearLayout.ivLoanRequested.setColorFilter(color)

                    holder.itemView.setOnClickListener {
                        listener(el)
                    }
                } ?: run {
                    holder.mLinearLayout.ivLoanRequested.visible(false)
                }
            }
        }
    }
}