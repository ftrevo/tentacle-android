package br.com.concrete.tentacle.features.library.loan

import android.os.Bundle
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.finishActivity
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.features.HostActivity
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_COVER_SMALL
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.activity_loan_success.btClose
import kotlinx.android.synthetic.main.activity_loan_success.btOk
import kotlinx.android.synthetic.main.activity_loan_success.ivCover
import kotlinx.android.synthetic.main.activity_loan_success.tvText

class LoanActivitySuccess : BaseActivity() {

    companion object {
        const val GAME_NAME_EXTRA = "gameNameExtra"
        const val GAME_NAME_EXTRA_ID = "gameImageIdExtra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_success)
        init()
    }

    private fun init() {

        if (intent.hasExtra(GAME_NAME_EXTRA)) {
            tvText.text = getString(R.string.loan_success, intent.getStringExtra(GAME_NAME_EXTRA))
        }
        if (intent.hasExtra(GAME_NAME_EXTRA_ID)) {
            ivCover.loadImageUrl(
                Utils.assembleGameImageUrl(
                    IMAGE_SIZE_TYPE_COVER_SMALL,
                    intent.getStringExtra(GAME_NAME_EXTRA_ID)
                )
            )
        }

        btClose.setOnClickListener {
            close()
        }

        btOk.setOnClickListener {
            close()
            HostActivity.fragment.value = R.id.navigate_to_my_reservations
        }
    }

    private fun close() {
        finishActivity(animation = ActivityAnimation.TRANSLATE_DOWN)
    }
}