package br.com.concrete.tentacle.features.myreservations.detail

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.utils.DialogUtils
import kotlinx.android.synthetic.main.activity_my_reservations_details.*
import kotlinx.android.synthetic.main.game_view_header_layout.ivGameStatus
import kotlinx.android.synthetic.main.game_view_header_layout.tvGameStatus
import kotlinx.android.synthetic.main.progress_include.progressBarList
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.Date

import java.util.concurrent.TimeUnit

class MyReservationActivity : BaseActivity() {

    companion object {
        const val LOAN_EXTRA_ID = "loanExtraId"
    }

    private val viewModel: MyReservationDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reservations_details)
        setupToolbar(R.string.toolbar_title_my_reservations_detail, R.drawable.ic_close)
        init()
        initObservable()
    }

    private fun init() {
        if (intent.hasExtra(LOAN_EXTRA_ID)) {
            val gameId = intent.extras?.getString(LOAN_EXTRA_ID)
            gameId?.let {
                viewModel.loadMyLoan(it)
            }
        }
    }

    private fun initObservable() {
        viewModel.getViewState().observe(this, Observer { base ->
            when (base.status) {
                ViewStateModel.Status.LOADING -> showProgress(true)
                ViewStateModel.Status.SUCCESS -> fillData(base.model)
                ViewStateModel.Status.ERROR -> showError(base.errors, getString(R.string.unknow_error))
            }
        })
    }

    private fun fillData(data: LoanResponse?) {
        showProgress(false)
        data?.let { loanResponse ->
            gameView.setGame(loanResponse.game)
            gameView.showStatusView(true)

            tvGamePlatform.text = loanResponse.media.platform.platformName
            tvGameOwner.text = loanResponse.mediaOwner.name

            val loanState = loanResponse.getLoanState()
            var loanText: String? = null
            var loanInfoText: String? = null
            var loanColor: Int? = null
            when (loanState) {
                LoanResponse.LoanState.ACTIVE -> {
                    data.estimatedReturnDate?.let { estimatedDate ->
                        val days = getLoanDaysToReturn(estimatedDate)
                        loanText = if (days > 1) {
                            getString(R.string.loan_state_active_plural, days)
                        } else {
                            getString(R.string.loan_state_active_singular, days)
                        }
                    }
                    loanColor = R.color.loan_state_active
                    loanInfoText = ""
                }
                LoanResponse.LoanState.EXPIRED -> {
                    loanText = getString(R.string.loan_state_expired)
                    loanColor = R.color.loan_state_expired
                    loanInfoText = getString(R.string.loan_info_expired)
                }
                LoanResponse.LoanState.PENDING -> {
                    loanText = getString(R.string.loan_state_pending)
                    loanColor = R.color.loan_state_pending
                    loanInfoText = getString(R.string.loan_info_pending)
                }
            }

            loanText?.let {
                tvGameStatus.text = loanText
            }

            loanInfoText?.let {
                tvLoanInfo.text = loanInfoText
            }
            val color = ContextCompat.getColor(this@MyReservationActivity, loanColor)
            ivGameStatus.setColorFilter(color)
            tvLoanInfo.setTextColor(color)
            group.visibility = View.VISIBLE
        } ?: run {
            val error = ErrorResponse()
            error.messageInt.add(R.string.load_error)
            showError(error, getString(R.string.unknow_error))
        }
    }

    private fun showProgress(show: Boolean) {
        progressBarList.visible(show)
    }

    override fun getFinishActivityTransition(): ActivityAnimation {
        return ActivityAnimation.TRANSLATE_DOWN
    }

    private fun getLoanDaysToReturn(estimatedReturnDate: String): Int {
        val estimated = estimatedReturnDate.toDate().timeInMillis
        val now = Date().time
        val diff = estimated - now
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
    }

    override fun showError(errors: ErrorResponse?, title: String) {
        errors?.let { errorResponse ->
            errorResponse.messageInt.map { error ->
                errorResponse.message.add(getString(error))
            }
            val ers = errorResponse.toString()

            DialogUtils.showDialog(
                context = this@MyReservationActivity,
                title = title,
                message = ers,
                positiveText = getString(R.string.ok),
                positiveListener = DialogInterface.OnClickListener { _, _ ->
                    finish()
                }
            )
        }
    }
}