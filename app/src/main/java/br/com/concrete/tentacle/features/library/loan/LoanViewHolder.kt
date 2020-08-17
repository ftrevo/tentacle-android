package br.com.concrete.tentacle.features.library.loan

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.game.Screenshot
import br.com.concrete.tentacle.data.models.library.Video
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.startActivityWithoutAnimation
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_LOGO_MED
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.item_game_video.view.imageView
import kotlinx.android.synthetic.main.item_game_video.view.ivBackground

class LoanViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    companion object {

        fun <T> bind(holder: RecyclerView.ViewHolder, any: T) {
            if (holder is LoanViewHolder) {
                when (any) {
                    is Video -> imageBackgroundYoutube(holder.itemView, any)
                    is Screenshot -> imageBackgroundScreenshots(holder.itemView, any)
                }
            }
        }

        private fun imageBackgroundYoutube(item: View, video: Video) {
            item.ivBackground.loadImageUrl(
                Utils.assembleGameImageUrlYouTube(
                    video.video_id
                )
            )

            item.setOnClickListener {
                val extras = Bundle()
                extras.putString(PinchToZoomActivity.ID, video.video_id)
                extras.putString(PinchToZoomActivity.TYPE, PinchToZoomActivity.VIDEO)
                startPinchActivity(item.context, extras)
            }
        }

        private fun imageBackgroundScreenshots(item: View, screenshot: Screenshot) {
            val idImage = screenshot.imageId
            item.ivBackground.loadImageUrl(
                Utils.assembleGameImageUrl(
                    sizeType = IMAGE_SIZE_TYPE_LOGO_MED,
                    imageId = idImage
                ),
                drawablePlaceholder = ContextCompat.getDrawable(
                    item.context,
                    R.drawable.ic_image_placeholder
                )
            )
            item.setOnClickListener {
                val extras = Bundle()
                extras.putString(PinchToZoomActivity.ID, idImage)
                extras.putString(PinchToZoomActivity.TYPE, PinchToZoomActivity.IMAGE)
                startPinchActivity(item.context, extras)
            }
            item.imageView.visible(false)
        }

        private fun startPinchActivity(context: Context, extras: Bundle) {
            (context as? AppCompatActivity)?.launchActivity<PinchToZoomActivity>(
                extras = extras,
                animation = ActivityAnimation.TRANSLATE_UP
            )
                ?: context.startActivityWithoutAnimation<PinchToZoomActivity>(extras)
        }

    }
}