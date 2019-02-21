package br.com.concrete.tentacle.features.lendgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.finishActivity
import kotlinx.android.synthetic.main.activity_lend_game_success.btCloseLend
import kotlinx.android.synthetic.main.activity_lend_game_success.btOk
import kotlinx.android.synthetic.main.activity_lend_game_success.tvText


class LendGameActivitySuccess : BaseActivity() {

    companion object {
        const val LOAN_EXTRA = "loanExtra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lend_game_success)
        init()
    }

    private fun init() {

        if (intent.hasExtra(LOAN_EXTRA)) {
            val args = intent.getSerializableExtra(LOAN_EXTRA) as LoanResponse
            //TODO FORMAT DATE
            tvText.text = String.format(getString(R.string.lend_success), args.requestedBy.name, args.requestedAt)
        }

        btCloseLend.setOnClickListener {
            finish()
        }

        btOk.setOnClickListener {
            finish()
        }
    }

    override fun getFinishActivityTransition(): ActivityAnimation {
        return ActivityAnimation.TRANSLATE_DOWN
    }
}
