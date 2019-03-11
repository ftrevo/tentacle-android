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
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.format
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.utils.DEFAULT_RETURN_DATE_IN_WEEKS
import br.com.concrete.tentacle.utils.LOAN_ACTION_LEND
import br.com.concrete.tentacle.utils.SIMPLE_DATE_OUTPUT_FORMAT
import kotlinx.android.synthetic.main.activity_lend_game.*
import kotlinx.android.synthetic.main.progress_include.progressBarList
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.Calendar

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
    }

    private fun lendSuccess(loanResponse: LoanResponse?) {
        loanResponse?.let {
            showLoading(false)
            val bundle = Bundle()
            bundle.putSerializable(LendGameActivitySuccess.LOAN_EXTRA, it)
            launchActivity<LendGameActivitySuccess>(extras = bundle, animation = ActivityAnimation.TRANSLATE_UP)
            finish()
        }
    }

    private fun fillData(media: Media?) {
        showLoading(false)

        media?.let { m ->
            activeLoan = m.activeLoan
            tvGameName.text = m.game?.name ?: ""
            tvRequestedBy.text = m.activeLoan?.requestedByName ?: ""
            val currentDate = Calendar.getInstance()
            currentDate.add(Calendar.WEEK_OF_MONTH, DEFAULT_RETURN_DATE_IN_WEEKS)
            tvDate.text = getString(R.string.date_return_prefix, currentDate.format(SIMPLE_DATE_OUTPUT_FORMAT))

            activeLoan?.let {
                btLendGame.setOnClickListener {
                    lendGame()
                }
            }

            if (activeLoan?.loanDate != null) {
                tvReservado.text = getString(R.string.reserved_by)
                btLendGame.visibility = View.GONE
            } else {
                btLendGame.visibility = View.VISIBLE
            }
        }

        group.visibility = View.VISIBLE
    }

    private fun lendGame() {
        activeLoan?.let {
            viewModelLendGame.updateMediaLoan(it._id, LoanActionRequest(LOAN_ACTION_LEND))
        }
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