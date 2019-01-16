package br.com.concrete.tentacle.features.searchGame

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.Game
import kotlinx.android.synthetic.main.item_game.view.*

class SearchGameViewHolder(private val item: View): RecyclerView.ViewHolder(item) {

    companion object {
        fun callBack(holder: RecyclerView.ViewHolder, element: Game) {
            if (holder is SearchGameViewHolder) {
                holder.item.game_name.text = element.title
                holder.item.game_media.visibility = View.GONE

                holder.item.setOnClickListener {

                }

            }
        }
    }
}