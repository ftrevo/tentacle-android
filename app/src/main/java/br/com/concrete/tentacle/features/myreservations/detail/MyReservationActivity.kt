package br.com.concrete.tentacle.features.myreservations.detail

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.features.myreservations.MyReservationBaseViewModel
import kotlinx.android.synthetic.main.activity_my_reservations_details.gameView
import kotlinx.android.synthetic.main.activity_my_reservations_details.tvGameOwner
import kotlinx.android.synthetic.main.activity_my_reservations_details.tvGamePlatform
import kotlinx.android.synthetic.main.game_view_header_layout.ivGameStatus
import kotlinx.android.synthetic.main.game_view_header_layout.tvGameStatus
import kotlinx.android.synthetic.main.progress_include.progressBarList
import org.koin.android.viewmodel.ext.android.viewModel

class MyReservationActivity : BaseActivity(){

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

    private fun init(){
        if(intent.hasExtra(LOAN_EXTRA_ID)){
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
                ViewStateModel.Status.ERROR -> {}
            }
        })
    }

    private fun fillData(data: LoanResponse?){
        showProgress(false)
        data?.let {
            gameView.setGame(it.game)

            tvGamePlatform.text = it.media.platform
            tvGameOwner.text = it.mediaOwner.name

            val loanState = viewModel.getLoanState(data.estimatedReturnDate, data.loanDate)
            var loanText : String? = null
            var loanColor : Int? = null
            when(loanState){
                MyReservationBaseViewModel.LoanState.ACTIVE -> {
                    data.estimatedReturnDate?.let { estimatedDate ->
                        val days = viewModel.getLoanDaysToReturn(estimatedDate)
                        loanText = if(days > 1){
                            getString(R.string.loan_state_active_plural, days)
                        }else {
                            getString(R.string.loan_state_active_singular, days)
                        }
                    }
                    loanColor = R.color.loan_state_active
                }
                MyReservationBaseViewModel.LoanState.EXPIRED -> {
                    loanText = getString(R.string.loan_state_expired)
                    loanColor = R.color.loan_state_expired
                }
                MyReservationBaseViewModel.LoanState.PENDING -> {
                    loanText = getString(R.string.loan_state_pending)
                    loanColor = R.color.loan_state_pending
                }
            }

            loanText?.let {
                tvGameStatus.text = loanText
            }

            ivGameStatus.setColorFilter(ContextCompat.getColor(this@MyReservationActivity, loanColor))


        } ?: run {
            showError()
        }
    }

    private fun showProgress(show: Boolean){
        progressBarList.visible(show)
    }

    private fun showError(){
        //TODO
    }

    override fun getFinishActivityTransition(): ActivityAnimation {
        return ActivityAnimation.TRANSLATE_DOWN
    }
}