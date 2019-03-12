package br.com.concrete.tentacle.features.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.extensions.progress
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_SCREENSHOT_HUGE
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.item_home_game.view.homeItemBackground
import kotlinx.android.synthetic.main.item_home_game.view.homeItemDescriptionTextView
import kotlinx.android.synthetic.main.item_home_game.view.homeItemGameRatinBar
import kotlinx.android.synthetic.main.item_home_game.view.homeItemTitleTextView

class HomeViewHolder(
    private val mLinearLayout: View
) : RecyclerView.ViewHolder(mLinearLayout) {

    companion object {
        fun callBack(holder: RecyclerView.ViewHolder, element: Game, listener: (Game) -> Unit) {
            if (holder is HomeViewHolder) {
                holder.mLinearLayout.let { itemView ->
                    element.screenshots?.firstOrNull()?.let { screenshot ->
                        itemView.homeItemBackground.loadImageUrl(
                            Utils.assembleGameImageUrl(
                                sizeType = IMAGE_SIZE_TYPE_SCREENSHOT_HUGE,
                                imageId = screenshot.imageId
                            )
                        )
                    }
                    itemView.homeItemTitleTextView.text = element.name
                    element.summary?.let { description ->
                        itemView.homeItemDescriptionTextView.text = description
                    }
                    element.rating?.let { rating ->
                        itemView.homeItemGameRatinBar.progress(rating)
                    }

                    holder.itemView.setOnClickListener {
                        listener(element)
                    }
                }
            }
        }
    }
}