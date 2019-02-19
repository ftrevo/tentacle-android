package br.com.concrete.tentacle.features.library.loan

import android.os.Bundle
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.finishActivity
import kotlinx.android.synthetic.main.activity_loan_success.btClose
import kotlinx.android.synthetic.main.activity_loan_success.btOk
import kotlinx.android.synthetic.main.activity_loan_success.tvText

class LoanActivitySuccess : BaseActivity() {

    companion object {
        const val GAME_NAME_EXTRA = "gameNameExtra"
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

        btClose.setOnClickListener {
            close()
        }

        btOk.setOnClickListener {
            close()
        }
    }

    private fun close() {
        finishActivity(animation = ActivityAnimation.TRANSLATE_DOWN)
    }
}