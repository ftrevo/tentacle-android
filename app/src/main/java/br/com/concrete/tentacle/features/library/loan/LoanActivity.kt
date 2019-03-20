package br.com.concrete.tentacle.features.library.loan

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.custom.ChipCustom
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.MediaLibrary
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.utils.DEFAULT_EXCEPTION_STATUS_CODE
import kotlinx.android.synthetic.main.activity_loan.btPerformLoan
import kotlinx.android.synthetic.main.activity_loan.chip360
import kotlinx.android.synthetic.main.activity_loan.chip3ds
import kotlinx.android.synthetic.main.activity_loan.chipContainer
import kotlinx.android.synthetic.main.activity_loan.chipOne
import kotlinx.android.synthetic.main.activity_loan.chipPs3
import kotlinx.android.synthetic.main.activity_loan.chipPs4
import kotlinx.android.synthetic.main.activity_loan.chipSwitch
import kotlinx.android.synthetic.main.activity_loan.gameView
import kotlinx.android.synthetic.main.activity_loan.recyclerView
import kotlinx.android.synthetic.main.activity_loan.error
import kotlinx.android.synthetic.main.activity_loan.group
import kotlinx.android.synthetic.main.activity_loan.spOwners
import kotlinx.android.synthetic.main.activity_loan.tvTitle2
import kotlinx.android.synthetic.main.progress_include.progressBarList
import org.koin.android.viewmodel.ext.android.viewModel

private const val ONLY_ONE_MEDIA = 1
private const val ONLY_ONE_OWNER = 1

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
        gameView.showStatusView(false)
        spOwners.setBackgroundResource(R.drawable.shape_border_corners_3dp)
        btPerformLoan.disable()

        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        intent?.let {
            if (it.hasExtra(ID_LIBRARY_EXTRA)) {
                val libraryId = it.getStringExtra(ID_LIBRARY_EXTRA)
                libraryId?.let { id ->

                    viewModel.getDetailsGame(id)
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
                ViewStateModel.Status.SUCCESS -> {
                    populateScreen(viewStateModel.model)
                    populateRecyclerView(
                        videos = viewStateModel.model?.videos,
                        screenshots = viewStateModel.model?.screenshots
                    )
                }
                ViewStateModel.Status.ERROR -> showInitError()
            }
        })

        viewModel.getLoan().observe(this, Observer { viewStateModel ->
            when (viewStateModel.status) {
                ViewStateModel.Status.LOADING -> showLoading(true)
                ViewStateModel.Status.SUCCESS -> showLoanSuccess()
                ViewStateModel.Status.ERROR -> {
                    showLoading(false)
                    viewStateModel.errors?.let {
                        if (it.statusCode != DEFAULT_EXCEPTION_STATUS_CODE) {
                            showError(viewStateModel.errors, getString(R.string.someone_was_faster))
                        } else {
                            showError(viewStateModel.errors)
                        }
                    }
                }
            }
        })

        viewModel.getGame().observe(this, Observer { viewStateModel ->
            when (viewStateModel.status) {
                ViewStateModel.Status.LOADING -> showLoading(true)
                ViewStateModel.Status.SUCCESS ->
                    viewStateModel.model?.let {
                        gameView.setGame(it)
                        viewModel.loadLibrary(it._id)
                    }
                ViewStateModel.Status.ERROR -> showInitError()
            }
        })
    }

    private fun <T> populateRecyclerView(videos: List<T>?, screenshots: List<T>?) {
        val list: ArrayList<T?> = ArrayList()
        videos?.let { list.addAll(it) }
        screenshots?.let { list.addAll(it) }

        if (list.size > 0) {
            recyclerView.setItemViewCacheSize(list.size)
            recyclerView.adapter = BaseAdapter(
                elements = list,
                layout = R.layout.item_game_video, holder = { view ->
                    LoanViewHolder(view)
                }, holderCallback = { holder, element ->
                    LoanViewHolder.bind(holder = holder, any = element)
                })
        }
    }

    private fun showLoanSuccess() {
        library?.let {
            val extras = Bundle()
            extras.putString(LoanActivitySuccess.GAME_NAME_EXTRA, it.name)
            extras.putString(LoanActivitySuccess.GAME_NAME_EXTRA_ID, it.cover?.imageId)
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
        group.visibility = View.VISIBLE
        error.visibility = View.GONE
        library?.let {
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

            autoSelectIfUniqueMedia()
        }
    }

    private fun setOwners(list: List<MediaLibrary>) {
        resetHit()
        spOwners.setItems(list)
        autoSelectIfUniqueOwner(list)
        spOwners.isEnabled = list.isNotEmpty()
        if (list.isEmpty()) {
            configureSpinnerFirstState()
        } else {
            val colorDisable = resources.getColor(R.color.white)
            tvTitle2.setTextColor(colorDisable)
            spOwners.setArrowColor(colorDisable)
        }
    }

    private fun resetHit() {
        spOwners.expand()
        spOwners.collapse()
    }

    private fun autoSelectIfUniqueOwner(list: List<MediaLibrary>) {
        if (list.size == ONLY_ONE_OWNER) {
            spOwners.selectedIndex = 0
            spOwners.isSelected = true
            mediaLibrary = spOwners.getItems<MediaLibrary>()[0]
            btPerformLoan.enable()
        } else {
            btPerformLoan.disable()
        }
    }

    private fun autoSelectIfUniqueMedia() {
        var count = 0
        var position = 0
        for (i in 0..chipContainer.childCount) {
            val view = chipContainer.getChildAt(i)
            if (view is ChipCustom) {
                if (view.visibility == View.VISIBLE) {
                    position = i
                    count++
                }
            }
        }
        if (count == ONLY_ONE_MEDIA) {
            val view = chipContainer.getChildAt(position)
            view.performClick()
            view.isPressed = true
        } else {
            configureSpinnerFirstState()
        }
    }

    private fun showInitError() {
        showLoading(false)
        error.visibility = View.VISIBLE
        error.setUpComponents(R.drawable.ilustra_tentacle, R.string.load_error, R.string.load_again)
        error.setUpActionErrorButton {
            init()
        }
    }

    override fun getFinishActivityTransition(): ActivityAnimation {
        return ActivityAnimation.TRANSLATE_DOWN
    }

    private fun configureSpinnerFirstState() {
        spOwners.hint = getString(R.string.select_hint)
        val colorDisable = resources.getColor(R.color.disableTextButton)
        spOwners.isEnabled = false

        tvTitle2.setTextColor(colorDisable)
        spOwners.setHintTextColor(colorDisable)
        spOwners.setArrowColor(colorDisable)
    }
}