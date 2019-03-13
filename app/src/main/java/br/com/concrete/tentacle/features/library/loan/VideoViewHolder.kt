package br.com.concrete.tentacle.features.library.loan

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.library.Video
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.item_game_video.view.ivBackground

class VideoViewHolder(val item: View) : RecyclerView.ViewHolder(item) {

    fun bind(video: Video) {
        item.let {
            item.ivBackground.loadImageUrl(
                Utils.assembleGameImageUrlYouTube(
                    video.video_id
                )
            )
        }
    }
}