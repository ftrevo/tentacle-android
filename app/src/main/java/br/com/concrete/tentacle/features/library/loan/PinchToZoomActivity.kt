package br.com.concrete.tentacle.features.library.loan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.finishActivity
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_ORIGINAL
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.activity_pinch_to_zoom.btClose
import kotlinx.android.synthetic.main.activity_pinch_to_zoom.photoView

class PinchToZoomActivity : AppCompatActivity() {

    companion object {
        const val ID_IMAGE = "ID_IMAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pinch_to_zoom)
        init()
    }

    private fun init() {
        intent?.let {
            if (it.hasExtra(ID_IMAGE)) {
                val idImage = it.getStringExtra(ID_IMAGE)
                idImage?.let {
                    photoView.loadImageUrl(
                        Utils.assembleGameImageUrl(
                            sizeType = IMAGE_SIZE_TYPE_ORIGINAL,
                            imageId = idImage
                        )
                    )
                }
            }
        }

        btClose.setOnClickListener {
            closeActivity()
        }
    }

    private fun closeActivity() {
        finishActivity(animation = ActivityAnimation.TRANSLATE_DOWN)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        closeActivity()
    }
}