package br.com.concrete.tentacle.features.library.loan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.game.Screenshot
import br.com.concrete.tentacle.data.models.library.Video
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_ORIGINAL
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.item_game_video.view.imageView
import kotlinx.android.synthetic.main.item_game_video.view.ivBackground

class LoanViewHolder(val item: View) : RecyclerView.ViewHolder(item) {

    private val context = item.context

    fun bind(any: Any) {
        item.let {
            when (any) {
                is Video -> imageBackgroundYoutube(any)
                is Screenshot -> imageBackgroundScreenshots(any)
            }
        }
    }

    private fun imageBackgroundYoutube(video: Video) {
        item.ivBackground.loadImageUrl(
            Utils.assembleGameImageUrlYouTube(
                video.video_id
            )
        )

        item.setOnClickListener {
            context?.startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        Utils.assembleUrlYouTube(video.video_id)
                    )
                )
            )
        }
    }

    private fun imageBackgroundScreenshots(screenshot: Screenshot) {
        val idImage = screenshot.imageId
        item.ivBackground.loadImageUrl(
            Utils.assembleGameImageUrl(
                sizeType = IMAGE_SIZE_TYPE_ORIGINAL,
                imageId = idImage
            )
        )
        item.setOnClickListener {
                val extras = Bundle()
            extras.putString(PinchToZoomActivity.ID_IMAGE, idImage)

            (context as AppCompatActivity)
                .launchActivity<PinchToZoomActivity>(
                    extras = extras,
                    animation = ActivityAnimation.TRANSLATE_UP
                )
        }
        item.imageView.visible(false)
    }
}