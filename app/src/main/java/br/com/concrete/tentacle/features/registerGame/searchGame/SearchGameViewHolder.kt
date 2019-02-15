package br.com.concrete.tentacle.features.registerGame.searchGame

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.Game
import kotlinx.android.synthetic.main.item_game.view.*
import kotlinx.android.synthetic.main.item_game_search.view.horizontalLine
import kotlinx.android.synthetic.main.item_game_search.view.mediaImageView

class SearchGameViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

    companion object {
        fun callBack(holder: RecyclerView.ViewHolder, game: Game, listener: (Game) -> Unit) {

            if (holder is SearchGameViewHolder) {
                if (game._id == Game.ID_EMPTY_GAME) {
                    visibleViews(holder)
                } else {
                    holder.item.game_name.text = game.title
                    holder.itemView.setOnClickListener {
                        listener(game)
                    }
                }
            }
        }

        private fun visibleViews(holder: SearchGameViewHolder) {
            holder.item.game_name.visibility = View.INVISIBLE
            holder.item.horizontalLine.visibility = View.INVISIBLE
            holder.item.mediaImageView.visibility = View.INVISIBLE
        }
    }
}