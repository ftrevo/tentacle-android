package br.com.concrete.tentacle.features.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.extensions.progress
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_SCREENSHOT_HUGE
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.item_home_game.view.homeItemBackground
import kotlinx.android.synthetic.main.item_home_game.view.homeItemDescriptionTextView
import kotlinx.android.synthetic.main.item_home_game.view.homeItemGameRatinBar
import kotlinx.android.synthetic.main.item_home_game.view.homeItemTitleTextView
import kotlinx.android.synthetic.main.item_home_game.view.tv360
import kotlinx.android.synthetic.main.item_home_game.view.tv3DS
import kotlinx.android.synthetic.main.item_home_game.view.tvNS
import kotlinx.android.synthetic.main.item_home_game.view.tvONE
import kotlinx.android.synthetic.main.item_home_game.view.tvPS3
import kotlinx.android.synthetic.main.item_home_game.view.tvPS4

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

                    itemView.tv360.visible(element.mediaXbox360Count > 0)
                    itemView.tv3DS.visible(element.mediaNintendo3dsCount > 0)
                    itemView.tvNS.visible(element.mediaNintendoSwitchCount > 0)
                    itemView.tvONE.visible(element.mediaXboxOneCount > 0)
                    itemView.tvPS3.visible(element.mediaPs3Count > 0)
                    itemView.tvPS4.visible(element.mediaPs4Count > 0)

                    holder.itemView.setOnClickListener {
                        listener(element)
                    }
                }
            }
        }
    }
}