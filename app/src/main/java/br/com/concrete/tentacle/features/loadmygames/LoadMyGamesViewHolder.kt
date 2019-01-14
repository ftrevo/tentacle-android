package br.com.concrete.tentacle.features.loadmygames

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.Media
import kotlinx.android.synthetic.main.fragment_game_item.view.*

class LoadMyGamesViewHolder(
    private val mLinearLayout: LinearLayout
) : RecyclerView.ViewHolder(mLinearLayout){

    companion object {
        fun <T> callBack(holder: RecyclerView.ViewHolder, element: T) {

            val gameViewHolder = holder as LoadMyGamesViewHolder
            val media = element as Media
            gameViewHolder.mLinearLayout.game_name.text = media.game.title
            gameViewHolder.mLinearLayout.game_media.text = media.platform

        }
    }

}