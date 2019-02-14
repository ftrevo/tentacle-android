package br.com.concrete.tentacle.features.library

import android.os.Bundle
import android.view.View
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.MediaLibrary
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

class LoanActivity : BaseActivity() {

    companion object {
        const val MEDIA_LIBRARY_EXTRA = "mediaLibraryExtra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)
        setupToolbar(R.string.toolbar_title_loan_game, R.drawable.ic_close)

        init()
    }

    private fun init() {
        spOwners.setBackgroundResource(R.drawable.shape_border_corners_3dp)
        btPerformLoan.disable()
        intent?.let {
            if (it.hasExtra(MEDIA_LIBRARY_EXTRA)) {
                val media = it.getParcelableExtra<Library>(MEDIA_LIBRARY_EXTRA)
                tvGameName.text = media.title
                setOwners(emptyList())

                chipPs3.visibility = if (media.mediaPs3.isEmpty()) View.GONE else View.VISIBLE
                chipPs4.visibility = if (media.mediaPs4.isEmpty()) View.GONE else View.VISIBLE
                chip360.visibility = if (media.mediaXbox360.isEmpty()) View.GONE else View.VISIBLE
                chipOne.visibility = if (media.mediaXboxOne.isEmpty()) View.GONE else View.VISIBLE
                chip3ds.visibility = if (media.mediaNintendo3ds.isEmpty()) View.GONE else View.VISIBLE
                chipSwitch.visibility = if (media.mediaNintendoSwitch.isEmpty()) View.GONE else View.VISIBLE

                chipContainer.setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {

                        R.id.chipPs3 -> setOwners(media.mediaPs3)
                        R.id.chipPs4 -> setOwners(media.mediaPs4)
                        R.id.chip360 -> setOwners(media.mediaXbox360)
                        R.id.chipOne -> setOwners(media.mediaXboxOne)
                        R.id.chip3ds -> setOwners(media.mediaNintendo3ds)
                        R.id.chipSwitch -> setOwners(media.mediaNintendoSwitch)
                        else -> {
                            setOwners(emptyList())
                            btPerformLoan.disable()
                        }
                    }
                }

                spOwners.setOnItemSelectedListener { view, position, id, item ->
                    btPerformLoan.enable()
                }

                btPerformLoan.setOnClickListener {
                    // TODO perform loan
                }
            }
        }
    }

    private fun setOwners(list: List<MediaLibrary>) {
        spOwners.setItems(list)
        spOwners.isEnabled = list.isNotEmpty()
    }
}