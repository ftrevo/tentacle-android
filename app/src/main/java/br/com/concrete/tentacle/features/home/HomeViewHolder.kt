package br.com.concrete.tentacle.features.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.Game
import kotlinx.android.synthetic.main.item_home_game.view.*

class HomeViewHolder(private val mLinearLayout: View
) : RecyclerView.ViewHolder(mLinearLayout) {

    companion object {
        fun callBack(holder: RecyclerView.ViewHolder, element: Game) {
            if (holder is HomeViewHolder) {
                holder.mLinearLayout.tvTitleGame.text = element.title
            }
        }
    }
}