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
        fun callBack(holder: RecyclerView.ViewHolder, element: Media, listener: (Media) -> Unit) {
            if (holder is LoadMyGamesViewHolder) {
                holder.mLinearLayout.game_name.text = element.gameData?.name ?: ""
                holder.mLinearLayout.game_media.text = element.platform.platformName
                if (element.activeLoan != null) {
                    holder.mLinearLayout.ivLoanRequested.visibility = View.VISIBLE

                    val color = if (element.activeLoan.loanDate == null) { // Loan was requested
                        ContextCompat.getColor(holder.mLinearLayout.context, R.color.colorAccent)
                    } else {
                        ContextCompat.getColor(holder.mLinearLayout.context, R.color.loanPerformed)
                    }
                    holder.mLinearLayout.ivLoanRequested.setColorFilter(color)
                } else {
                    holder.mLinearLayout.ivLoanRequested.visible(false)
                }

                holder.itemView.setOnClickListener {
                    listener(element)
                }
            }
        }
    }
}