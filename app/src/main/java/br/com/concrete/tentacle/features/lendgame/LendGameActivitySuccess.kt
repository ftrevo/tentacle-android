package br.com.concrete.tentacle.features.lendgame

import android.os.Bundle
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.models.RememberDeliveryResponse
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.format
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.features.HostActivity
import br.com.concrete.tentacle.utils.LOAN_ACTION_LEND
import br.com.concrete.tentacle.utils.LOAN_ACTION_REMEMBER_DELIVERY
import br.com.concrete.tentacle.utils.LOAN_ACTION_RETURN
import br.com.concrete.tentacle.utils.SIMPLE_DATE_OUTPUT_FORMAT
import kotlinx.android.synthetic.main.activity_lend_game_success.btCloseLend
import kotlinx.android.synthetic.main.activity_lend_game_success.btOk
import kotlinx.android.synthetic.main.activity_lend_game_success.tvText

class LendGameActivitySuccess : BaseActivity() {

    companion object {
        const val LOAN_EXTRA = "loanExtra"
        const val DELIVERY_RESPONSE_EXTRA = "deliveryResponseExtra"
        const val ACTION_EXTRA = "actionExtra"
    }

    private var action: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lend_game_success)
        init()
        initEvents()
    }

    private fun init() {

        if (intent.hasExtra(ACTION_EXTRA)) {
            action = intent.getStringExtra(ACTION_EXTRA)
            when (action) {
                LOAN_ACTION_LEND -> lendSuccess()
                LOAN_ACTION_RETURN -> returnSuccess()
                LOAN_ACTION_REMEMBER_DELIVERY -> rememberSuccess()
            }
        }
    }

    private fun initEvents() {
        btCloseLend.setOnClickListener {
            finish()
        }

        btOk.setOnClickListener {
            finish()
            action?.let {
                HostActivity.fragment.value = R.id.navigate_to_my_games
            }
        }
    }

    private fun lendSuccess() {
        if (intent.hasExtra(LOAN_EXTRA)) {
            val args = intent.getParcelableExtra<LoanResponse>(LOAN_EXTRA)
            args.estimatedReturnDate?.let {
                tvText.text = String.format(getString(R.string.lend_success), args.requestedBy.name, it.toDate().format(SIMPLE_DATE_OUTPUT_FORMAT))
                btOk.setButtonName(getString(R.string.back_to_my_games))
            }
        }
    }

    private fun returnSuccess() {
        tvText.text = getString(R.string.return_success)
        btOk.setButtonName(getString(R.string.back_to_my_games))
    }

    private fun rememberSuccess() {
        if (intent.hasExtra(DELIVERY_RESPONSE_EXTRA)) {
            val args = intent.getParcelableExtra<RememberDeliveryResponse>(DELIVERY_RESPONSE_EXTRA)
            args?.let {
                tvText.text = String.format(getString(R.string.remember_success), args.requestedBy.name)
                btOk.setButtonName(getString(R.string.back_to_my_games))
            }
        }
    }

    override fun getFinishActivityTransition(): ActivityAnimation {
        return ActivityAnimation.TRANSLATE_DOWN
    }
}
