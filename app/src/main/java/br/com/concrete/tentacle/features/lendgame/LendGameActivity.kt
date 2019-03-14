package br.com.concrete.tentacle.features.lendgame

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.models.ActiveLoan
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.LoanActionRequest
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.RememberDeliveryResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.format
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.utils.LOAN_ACTION_LEND
import br.com.concrete.tentacle.utils.LOAN_ACTION_REMEMBER_DELIVERY
import br.com.concrete.tentacle.utils.LOAN_ACTION_RETURN
import br.com.concrete.tentacle.utils.SIMPLE_DATE_OUTPUT_FORMAT
import kotlinx.android.synthetic.main.activity_lend_game.*
import kotlinx.android.synthetic.main.progress_include.progressBarList
import org.koin.android.viewmodel.ext.android.viewModel

class LendGameActivity : BaseActivity() {

    companion object {
        const val MEDIA_ID_EXTRA = "mediaIdExtra"
    }

    private val viewModelLendGame: LendGameViewModel by viewModel()
    private var activeLoan: ActiveLoan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lend_game)
        setupToolbar(R.string.toolbar_title_lend_game, R.drawable.ic_close, true)
        initEvents()
        initObserver()
        loadData()
    }

    private fun loadData() {
        intent?.let {
            if (it.hasExtra(MEDIA_ID_EXTRA)) {
                val id = it.getStringExtra(MEDIA_ID_EXTRA)
                viewModelLendGame.fetchMediaLoan(id)
            }
        }
    }

    private fun initEvents(){
        btLendGame.setOnClickListener {
            lendGame()
        }

        btRequestReturn.setOnClickListener{
            rememberDelivery()
        }
    }

    private fun initObserver() {
        viewModelLendGame.getMediaViewState().observe(this, Observer { stateModel ->
            when (stateModel.status) {
                ViewStateModel.Status.SUCCESS -> fillData(stateModel.model)
                ViewStateModel.Status.LOADING -> showLoading(true)
                ViewStateModel.Status.ERROR -> showError(stateModel.errors)
            }
        })

        viewModelLendGame.getUpdateLoanViewState().observe(this, Observer { stateModel ->
            when (stateModel.status) {
                ViewStateModel.Status.SUCCESS -> lendSuccess(stateModel.model)
                ViewStateModel.Status.LOADING -> showLoading(true)
                ViewStateModel.Status.ERROR -> showError(stateModel.errors)
            }
        })

        viewModelLendGame.getRememberDeliveryViewState().observe(this, Observer { stateModel ->
            when (stateModel.status) {
                ViewStateModel.Status.SUCCESS -> rememberDeliverySuccess(stateModel.model)
                ViewStateModel.Status.LOADING -> showLoading(true)
                ViewStateModel.Status.ERROR -> showError(stateModel.errors)
            }
        })
    }

    private fun rememberDeliverySuccess(deliveryResponse: RememberDeliveryResponse?) {
        showLoading(false)
        deliveryResponse?.let {
            val bundle = Bundle()
            bundle.putString(LendGameActivitySuccess.ACTION_EXTRA, LOAN_ACTION_REMEMBER_DELIVERY)
            bundle.putParcelable(LendGameActivitySuccess.DELIVERY_RESPONSE_EXTRA, it)
            launchActivitySuccess(bundle)
        }
    }

    private fun lendSuccess(loanResponse: LoanResponse?) {
        showLoading(false)
        loanResponse?.let {
            val action = if(it.returnDate == null) LOAN_ACTION_LEND else LOAN_ACTION_RETURN
            val bundle = Bundle()
            bundle.putSerializable(LendGameActivitySuccess.LOAN_EXTRA, it)
            bundle.putString(LendGameActivitySuccess.ACTION_EXTRA, action)
            launchActivitySuccess(bundle)
        }
    }

    private fun launchActivitySuccess(bundle: Bundle){
        launchActivity<LendGameActivitySuccess>(extras = bundle, animation = ActivityAnimation.TRANSLATE_UP)
        finish()
    }

    private fun fillData(media: Media?) {
        showLoading(false)

        media?.let { m ->
            activeLoan = m.activeLoan
            m.game?.let { game ->
                gameView.setGame(game)
            }
            gameView.showStatusView(false)
            tvRequestedBy.text = m.activeLoan?.requestedByName ?: ""


            activeLoan?.let { activeLoan ->
                btLendGame.visible(true)

                tvRequestedBy.text = activeLoan.requestedByName
                tvDateRequested.text = getString(R.string.date_requested_prefix, activeLoan.requestedAt.toDate().format(SIMPLE_DATE_OUTPUT_FORMAT))

                activeLoan.loanDate?.let {
                    tvReservado.text = getString(R.string.reserved_by)
                    btRequestReturn.visible(true)
                    btLendGame.setButtonName(getString(R.string.gameReturned))

                    val expired = activeLoan.isExpired()

                    tvExpired.visible(expired)

                    if(expired){

                        btRequestReturn.enable()
                    }else{
                        btRequestReturn.disable()
                    }
                } ?: run{
                    btRequestReturn.visible(false)
                }
            }

            val returnDate = activeLoan?.getReturnDate() ?: ActiveLoan.getDefaultReturnDate()
            tvDate.text =  getString(R.string.date_return_prefix, returnDate.format(SIMPLE_DATE_OUTPUT_FORMAT))
        }

        group.visibility = View.VISIBLE
    }

    private fun lendGame() {
        activeLoan?.let {
            val action = if (it.loanDate == null) LoanActionRequest(LOAN_ACTION_LEND) else LoanActionRequest(LOAN_ACTION_RETURN)
            viewModelLendGame.updateMediaLoan(it._id, action)
        }
    }

    private fun rememberDelivery(){
        viewModelLendGame.rememberDelivery()
    }

    override fun getFinishActivityTransition(): ActivityAnimation {
        return ActivityAnimation.TRANSLATE_DOWN
    }

    private fun showLoading(show: Boolean) {
        progressBarList.visible(show)
    }

    override fun showError(errors: ErrorResponse?, title: String) {
        showLoading(false)
        super.showError(errors, title)
    }
}