package br.com.concrete.tentacle.features.loadmygames

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.Media
import kotlinx.android.synthetic.main.item_game.view.*

class LoadMyGamesViewHolder(
    private val mLinearLayout: View
) : RecyclerView.ViewHolder(mLinearLayout) {

    companion object {
        fun callBack(holder: RecyclerView.ViewHolder, element: Media) {
            if (holder is LoadMyGamesViewHolder) {
                holder.mLinearLayout.game_name.text = element.game?.title
                holder.mLinearLayout.game_media.text = element.showPlatformName()
            }
        }
    }
}