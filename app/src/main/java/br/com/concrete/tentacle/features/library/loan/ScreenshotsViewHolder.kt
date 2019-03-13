package br.com.concrete.tentacle.features.library.loan

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.game.Screenshot
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_ORIGINAL
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.item_game_screenshot.view.ivBackground

class ScreenshotsViewHolder(val item: View) : RecyclerView.ViewHolder(item) {

    fun bind(screenshot: Screenshot) {
        item.let {
            item.ivBackground.loadImageUrl(
                Utils.assembleGameImageUrl(
                    sizeType = IMAGE_SIZE_TYPE_ORIGINAL,
                    imageId = screenshot.imageId
                )
            )
        }
    }

}