package br.com.concrete.tentacle.features.library.loan

import android.os.Bundle
import android.util.Log
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.finishActivity
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_ORIGINAL
import br.com.concrete.tentacle.utils.Utils
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_pinch_to_zoom.youtube_player
import kotlinx.android.synthetic.main.activity_pinch_to_zoom.photoView
import kotlinx.android.synthetic.main.activity_pinch_to_zoom.btClose

class PinchToZoomActivity : YouTubeBaseActivity() {

    private lateinit var onInitializedListener: YouTubePlayer.OnInitializedListener

    companion object {
        const val VIDEO = "VIDEO"
        const val IMAGE = "IMAGE"
        const val TYPE = "TYPE"
        const val ID = "ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pinch_to_zoom)
        init()
    }

    private fun init() {
        intent?.let { intent ->
            if (intent.hasExtra(TYPE)) {
                when (intent.getStringExtra(TYPE)) {
                    VIDEO -> loadVideo()
                    IMAGE -> loadImage()
                    else -> closeActivity()
                }
            }
        }

        btClose.setOnClickListener {
            closeActivity()
        }
    }

    private fun loadImage() {
        youtube_player.visible(false)
        photoView.visible(true)
        val imageId = intent.getStringExtra(ID)
        imageId?.let {
            photoView.loadImageUrl(
                Utils.assembleGameImageUrl(
                    sizeType = IMAGE_SIZE_TYPE_ORIGINAL,
                    imageId = imageId
                )
            )
        }

    }

    private fun loadVideo() {
        photoView.visible(false)
        youtube_player.visible(true)
        val videoId = intent.getStringExtra(ID)
        videoId?.let {
            initializeVideo(videoId)
        }

    }

    private fun closeActivity() {
        finishActivity(animation = ActivityAnimation.TRANSLATE_DOWN)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        closeActivity()
    }

    private fun initializeVideo(videoId: String) {
        onInitializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                youtubePlayer: YouTubePlayer?,
                p2: Boolean
            ) {
                youtubePlayer?.loadVideo(videoId)
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                result: YouTubeInitializationResult?
            ) {
                Log.e("YOUTUBE-PLAYER ERROR: ", result.toString())
            }
        }
        youtube_player.initialize(
            getString(R.string.youtube_api_key),
            onInitializedListener
        )
    }
}