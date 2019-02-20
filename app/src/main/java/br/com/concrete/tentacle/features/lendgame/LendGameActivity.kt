package br.com.concrete.tentacle.features.lendgame

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.format
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.utils.DEFAULT_RETURN_DATE_IN_WEEKS
import br.com.concrete.tentacle.utils.SIMPLE_DATE_OUTPUT_FORMAT
import kotlinx.android.synthetic.main.activity_lend_game.tvDate
import kotlinx.android.synthetic.main.activity_lend_game.tvGameName
import kotlinx.android.synthetic.main.activity_lend_game.tvRequestedBy
import kotlinx.android.synthetic.main.progress_include.progressBarList
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.Calendar

class LendGameActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val MEDIA_ID_EXTRA = "mediaIdExtra"
    }

    private val viewModelLendGame: LendGameViewModel by viewModel()

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
    }

    private fun fillData(media: Media?) {
        showLoading(false)
        media?.let { m ->
            tvGameName.text = m.gameData?.name ?: ""
            tvRequestedBy.text = m.activeLoan?.requestedByName ?: ""
            val currentDate = Calendar.getInstance()
            currentDate.add(Calendar.WEEK_OF_MONTH, DEFAULT_RETURN_DATE_IN_WEEKS)
            tvDate.text = getString(R.string.date_return_prefix, currentDate.format(SIMPLE_DATE_OUTPUT_FORMAT))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btLendGame -> lendGame()
        }
    }

    private fun lendGame() {
        // TODO Sprint 5
    }

    override fun getFinishActivityTransition(): ActivityAnimation {
        return ActivityAnimation.TRANSLATE_DOWN
    }

    private fun showLoading(show: Boolean) {
        progressBarList.visible(show)
    }

    override fun showError(errors: ErrorResponse?) {
        showLoading(false)
        super.showError(errors)
    }
}