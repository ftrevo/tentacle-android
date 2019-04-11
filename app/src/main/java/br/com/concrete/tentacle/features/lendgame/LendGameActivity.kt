package br.com.concrete.tentacle.features.lendgame

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import br.com.concrete.tentacle.utils.DialogUtils
import br.com.concrete.tentacle.utils.LOAN_ACTION_LEND
import br.com.concrete.tentacle.utils.LOAN_ACTION_REMEMBER_DELIVERY
import br.com.concrete.tentacle.utils.LOAN_ACTION_RETURN
import br.com.concrete.tentacle.utils.SIMPLE_DATE_OUTPUT_FORMAT
import kotlinx.android.synthetic.main.activity_lend_game.bottomGroup
import kotlinx.android.synthetic.main.activity_lend_game.btLendGame
import kotlinx.android.synthetic.main.activity_lend_game.btRequestReturn
import kotlinx.android.synthetic.main.activity_lend_game.gameView
import kotlinx.android.synthetic.main.activity_lend_game.group
import kotlinx.android.synthetic.main.activity_lend_game.tvDate
import kotlinx.android.synthetic.main.activity_lend_game.tvDateRequested
import kotlinx.android.synthetic.main.activity_lend_game.tvExpired
import kotlinx.android.synthetic.main.activity_lend_game.tvRequestedBy
import kotlinx.android.synthetic.main.activity_lend_game.tvReservado
import kotlinx.android.synthetic.main.progress_include.progressBarList
import org.koin.android.viewmodel.ext.android.viewModel

class LendGameActivity : BaseActivity() {

    companion object {
        const val MEDIA_ID_EXTRA = "mediaIdExtra"
    }

    private val viewModelLendGame: LendGameViewModel by viewModel()
    private var activeLoan: ActiveLoan? = null
    private var media: Media? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lend_game)
        setupToolbar(R.string.toolbar_title_lend_game, R.drawable.ic_close, true)
        initEvents()
        initObserver()
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_game_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.delete).let { menuItem ->
            media?.let {
                if (!it.active) menuItem?.title = getString(R.string.reactivate)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun loadData() {
        intent?.let {
            if (it.hasExtra(MEDIA_ID_EXTRA)) {
                val id = it.getStringExtra(MEDIA_ID_EXTRA)
                viewModelLendGame.fetchMediaLoan(id)
            }
        }
    }

    private fun initEvents() {
        btLendGame.setOnClickListener {
            lendGame()
        }

        btRequestReturn.setOnClickListener {
            rememberDelivery(activeLoan?._id)
        }
    }

    private fun initObserver() {
        viewModelLendGame.getMediaViewState().observe(this, Observer { stateModel ->
            when (stateModel.status) {
                ViewStateModel.Status.SUCCESS -> {
                    fillData(stateModel.model)
                    media = stateModel.model
                }
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

        viewModelLendGame.deleteMedia().observe(this, Observer { stateModel ->
            stateModel.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        this.finish()
                    }
                    ViewStateModel.Status.LOADING -> showLoading(true)
                    ViewStateModel.Status.ERROR -> showError(it.errors)
                }
            }
        })

        viewModelLendGame.activeMediaState().observe(this, Observer {
            it.getContentIfNotHandler()?.let { stateModel ->
                when (stateModel.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        finish()
                    }
                    ViewStateModel.Status.LOADING -> showLoading(true)
                    ViewStateModel.Status.ERROR -> {
                    }
                }
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
            val action = if (it.returnDate == null) LOAN_ACTION_LEND else LOAN_ACTION_RETURN
            val bundle = Bundle()
            bundle.putParcelable(LendGameActivitySuccess.LOAN_EXTRA, it)
            bundle.putString(LendGameActivitySuccess.ACTION_EXTRA, action)
            launchActivitySuccess(bundle)
        }
    }

    private fun launchActivitySuccess(bundle: Bundle) {
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
                bottomGroup.visible(true)
                btLendGame.visible(true)

                tvRequestedBy.text = activeLoan.requestedByName
                tvDateRequested.text = getString(
                    R.string.date_requested_prefix,
                    activeLoan.requestedAt.toDate().format(SIMPLE_DATE_OUTPUT_FORMAT)
                )

                activeLoan.loanDate?.let {
                    tvReservado.text = getString(R.string.reserved_by)
                    btRequestReturn.visible(true)
                    btLendGame.setButtonName(getString(R.string.gameReturned))

                    val expired = activeLoan.isExpired()

                    tvExpired.visible(expired)

                    if (expired) {

                        btRequestReturn.enable()
                    } else {
                        btRequestReturn.disable()
                    }
                } ?: run {
                    btRequestReturn.visible(false)
                }
            } ?: run {
                bottomGroup.visible(false)
            }

            val returnDate =
                activeLoan?.estimatedReturnDate?.toDate() ?: ActiveLoan.getDefaultReturnDate()
            tvDate.text = getString(R.string.date_return_prefix, returnDate.format(SIMPLE_DATE_OUTPUT_FORMAT))
        }

        invalidateOptionsMenu()

        group.visibility = View.VISIBLE
    }

    private fun lendGame() {
        activeLoan?.let {
            val action =
                if (it.loanDate == null) LoanActionRequest(LOAN_ACTION_LEND) else LoanActionRequest(LOAN_ACTION_RETURN)
            viewModelLendGame.updateMediaLoan(it._id, action)
        }
    }

    private fun rememberDelivery(id: String?) {
        viewModelLendGame.rememberDelivery(id)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                showDialogDelete()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    private fun showDialogDelete() {
        media?.let { media ->
            val mediaActive = media.active
            val gameName = String.format(
                if (mediaActive) getString(R.string.delete_dialog_message)
                else getString(R.string.reactivate_media_dialog_message), media.game?.name ?: ""
            )

            DialogUtils.showDialog(
                contentView = R.layout.custom_dialog_error,
                context = this,
                title = if (mediaActive) getString(R.string.delete_dialog_title) else getString(R.string.reactivate_media),
                message = gameName,
                positiveText = if (mediaActive) getString(R.string.delete) else getString(R.string.reactivate),
                positiveListener = DialogInterface.OnClickListener { _, _ ->
                    if (mediaActive) {
                        viewModelLendGame.deleteGame(media._id)
                    } else {
                        media.game?.let {
                            viewModelLendGame.activeMedia(
                                media,
                                true
                            )
                        }
                    }
                },
                negativeText = getString(R.string.not_delete)
            )
        }
    }
}