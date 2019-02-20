package br.com.concrete.tentacle.features.library.loan

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.MediaLibrary
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.extensions.visible
import kotlinx.android.synthetic.main.activity_loan.btPerformLoan
import kotlinx.android.synthetic.main.activity_loan.chip360
import kotlinx.android.synthetic.main.activity_loan.chip3ds
import kotlinx.android.synthetic.main.activity_loan.chipContainer
import kotlinx.android.synthetic.main.activity_loan.chipOne
import kotlinx.android.synthetic.main.activity_loan.chipPs3
import kotlinx.android.synthetic.main.activity_loan.chipPs4
import kotlinx.android.synthetic.main.activity_loan.chipSwitch
import kotlinx.android.synthetic.main.activity_loan.spOwners
import kotlinx.android.synthetic.main.activity_loan.tvGameName
import kotlinx.android.synthetic.main.progress_include.progressBarList
import org.koin.android.viewmodel.ext.android.viewModel

class LoanActivity : BaseActivity() {

    private val viewModel: LoanViewModel by viewModel()
    private var mediaLibrary: MediaLibrary? = null
    private var library: Library? = null

    companion object {
        const val ID_LIBRARY_EXTRA = "mediaLibraryExtra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)
        setupToolbar(R.string.toolbar_title_loan_game, R.drawable.ic_close)

        init()
        initObserver()
    }

    private fun init() {
        spOwners.setBackgroundResource(R.drawable.shape_border_corners_3dp)
        btPerformLoan.disable()
        intent?.let {
            if (it.hasExtra(ID_LIBRARY_EXTRA)) {
                val libraryId = it.getStringExtra(ID_LIBRARY_EXTRA)
                libraryId?.let { id ->
                    viewModel.loadLibrary(id)
                }

                spOwners.setOnItemSelectedListener { _, position, _, _ ->
                    mediaLibrary = spOwners.getItems<MediaLibrary>()[position]
                    btPerformLoan.enable()
                }

                btPerformLoan.setOnClickListener {
                    mediaLibrary?.let { media ->
                        viewModel.performLoad(media._id)
                    }
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.getLibrary().observe(this, Observer { viewStateModel ->
            when (viewStateModel.status) {
                ViewStateModel.Status.LOADING -> showLoading(true)
                ViewStateModel.Status.SUCCESS -> populateScreen(viewStateModel.model)
                ViewStateModel.Status.ERROR -> showError(viewStateModel.errors)
            }
        })

        viewModel.getLoan().observe(this, Observer { viewStateModel ->
            when (viewStateModel.status) {
                ViewStateModel.Status.LOADING -> showLoading(true)
                ViewStateModel.Status.SUCCESS -> showLoanSuccess()
                ViewStateModel.Status.ERROR -> showError(viewStateModel.errors)
            }
        })
    }

    private fun showLoanSuccess() {
        library?.let {
            val extras = Bundle()
            extras.putString(LoanActivitySuccess.GAME_NAME_EXTRA, it.name)
            launchActivity<LoanActivitySuccess>(extras = extras, animation = ActivityAnimation.TRANSLATE_UP)
            finish()
        }
    }

    private fun showLoading(show: Boolean) {
        progressBarList.visible(show)
    }

    private fun populateScreen(library: Library?) {
        this.library = library
        showLoading(false)
        library?.let {
            tvGameName.text = library.name
            setOwners(emptyList())

            chipPs3.visibility = if (library.mediaPs3.isEmpty()) View.GONE else View.VISIBLE
            chipPs4.visibility = if (library.mediaPs4.isEmpty()) View.GONE else View.VISIBLE
            chip360.visibility = if (library.mediaXbox360.isEmpty()) View.GONE else View.VISIBLE
            chipOne.visibility = if (library.mediaXboxOne.isEmpty()) View.GONE else View.VISIBLE
            chip3ds.visibility = if (library.mediaNintendo3ds.isEmpty()) View.GONE else View.VISIBLE
            chipSwitch.visibility = if (library.mediaNintendoSwitch.isEmpty()) View.GONE else View.VISIBLE

            chipContainer.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {

                    R.id.chipPs3 -> setOwners(library.mediaPs3)
                    R.id.chipPs4 -> setOwners(library.mediaPs4)
                    R.id.chip360 -> setOwners(library.mediaXbox360)
                    R.id.chipOne -> setOwners(library.mediaXboxOne)
                    R.id.chip3ds -> setOwners(library.mediaNintendo3ds)
                    R.id.chipSwitch -> setOwners(library.mediaNintendoSwitch)
                    else -> {
                        setOwners(emptyList())
                        btPerformLoan.disable()
                    }
                }
            }
        }
    }

    private fun setOwners(list: List<MediaLibrary>) {
        spOwners.setItems(list)
        spOwners.isEnabled = list.isNotEmpty()
    }

    override fun showError(errors: ErrorResponse?) {
        showLoading(false)
        super.showError(errors)
    }

    override fun getFinishActivityTransition(): ActivityAnimation {
        return ActivityAnimation.TRANSLATE_DOWN
    }
}