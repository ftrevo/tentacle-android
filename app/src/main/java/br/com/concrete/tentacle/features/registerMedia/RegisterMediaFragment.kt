package br.com.concrete.tentacle.features.registerMedia

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.callSnackbar
import br.com.concrete.tentacle.utils.ARGUMENT_GAME
import br.com.concrete.tentacle.utils.EMPTY_STRING
import kotlinx.android.synthetic.main.fragment_register_media.*
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterMediaFragment : BaseFragment() {

    private val viewModel: RegisterMediaViewModel by viewModel()
    private lateinit var game: Game
    private var selectedPlatform = EMPTY_STRING

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_register_media, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        game = arguments?.getParcelable<Game>(ARGUMENT_GAME)!!
        mediaNameTextView.text = game.title

        initListeners()
        initObservers()
    }

    private fun initListeners() {
        mediaRegisterRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedPlatform = when (checkedId) {
                mediaPS3RadioButton.id -> getString(R.string.register_media_ps3_radio_button_title)
                mediaPS4RadioButton.id -> getString(R.string.register_media_ps4_radio_button_title)
                mediaXbox360RadioButton.id -> getString(R.string.register_media_xbox360_radio_button_title)
                mediaXboxOneRadioButton.id -> getString(R.string.register_media_xboxone_radio_button_title)
                media3DSRadioButton.id -> getString(R.string.register_media_3ds_radio_button_title)
                mediaSwitchRadioButton.id -> getString(R.string.register_media_switch_radio_button_title)
                else -> EMPTY_STRING
            }
        }

        mediaRegisterButton.setOnClickListener {
            if (checkField()) viewModel.registerMedia(selectedPlatform, game)
            else context?.callSnackbar(view!!, getString(R.string.register_media_no_selection_error))
        }
    }

    private fun initObservers() {
        viewModel.viewStatusModel.observe(this, Observer { viewStatus ->
            viewStatus?.let {
                when (viewStatus.status) {
                    ViewStateModel.Status.LOADING -> {
                        mediaRegisterButton.isLoading(true)
                    }
                    ViewStateModel.Status.SUCCESS -> {
                        mediaRegisterButton.isLoading(false)
                        // TODO - navigate to my games screen
                    }
                    ViewStateModel.Status.ERROR -> {
                        mediaRegisterButton.isLoading(false)
                        showError(viewStatus.errors)
                    }
                }
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun checkField(): Boolean =
        !TextUtils.isEmpty(selectedPlatform)
}