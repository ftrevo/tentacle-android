package br.com.concrete.tentacle.features.loadmygames

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.extensions.visible
import kotlinx.android.synthetic.main.item_game.view.game_media
import kotlinx.android.synthetic.main.item_game.view.game_name
import kotlinx.android.synthetic.main.item_game.view.ivLoanRequested

class LoadMyGamesViewHolder(
    private val mLinearLayout: View
) : RecyclerView.ViewHolder(mLinearLayout) {

    companion object {
        var itemRemove = 0

        fun callBack(holder: RecyclerView.ViewHolder, el: Media, listener: (Media) -> Unit, listenerLongClick: (Media) -> Unit) {
            if (holder is LoadMyGamesViewHolder) {

                holder.itemView.setOnClickListener {
                    listener(el)
                }

                if (el._id == Media.ID_EMPTY_MEDIA) {
                    visibleView(holder, false)
                } else {
                    holder.mLinearLayout.game_name.text = el.game?.name ?: ""
                    holder.mLinearLayout.game_media.text = el.platform.platformName
                    holder.mLinearLayout.setOnLongClickListener {
                        itemRemove = holder.position
                        listenerLongClick(el)
                        true
                    }
                    el.activeLoan?.let {
                        holder.mLinearLayout.ivLoanRequested.visibility = View.VISIBLE
                        val color = if (it.loanDate == null) { // Loan was requested
                            ContextCompat.getColor(holder.mLinearLayout.context, R.color.colorAccent)
                        } else {
                            ContextCompat.getColor(holder.mLinearLayout.context, R.color.loanPerformed)
                        }
                        holder.mLinearLayout.ivLoanRequested.setColorFilter(color)

                    } ?: run {
                        holder.mLinearLayout.ivLoanRequested.visible(false)
                    }
                    visibleView(holder, true)
                }
            }
        }

        private fun visibleView(holder: LoadMyGamesViewHolder, isVisible: Boolean) {
            val state = if (isVisible) View.VISIBLE else View.INVISIBLE
            holder.mLinearLayout.visibility = state
        }
    }
}