package br.com.concrete.tentacle.features.registerGame.registerMedia

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
import br.com.concrete.tentacle.extensions.toPlatformName
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

    override fun getToolbarTitle(): Int {
        return R.string.toolbar_title_home
    }

    private fun initViews() {
        arguments?.let { bundle ->
            game = RegisterMediaFragmentArgs.fromBundle(bundle).gameArgument
            mediaRegisterNameTextView.text = game.title

            initListeners()
            initObservers()
        }
    }

    private fun initListeners() {
        mediaRegisterRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedPlatform = when (checkedId) {
                mediaPS3RadioButton.id -> getString(R.string.register_media_ps3_radio_button_title).toPlatformName()
                mediaPS4RadioButton.id -> getString(R.string.register_media_ps4_radio_button_title).toPlatformName()
                mediaXbox360RadioButton.id -> getString(R.string.register_media_xbox360_radio_button_title).toPlatformName()
                mediaXboxOneRadioButton.id -> getString(R.string.register_media_xboxone_radio_button_title).toPlatformName()
                media3DSRadioButton.id -> getString(R.string.register_media_3ds_radio_button_title).toPlatformName()
                mediaSwitchRadioButton.id -> getString(R.string.register_media_switch_radio_button_title).toPlatformName()
                else -> EMPTY_STRING
            }
        }

        mediaRegisterButton.setOnClickListener {
            if (checkField()) viewModel.registerMedia(selectedPlatform, game)
            else context?.callSnackbar(view!!, getString(R.string.register_media_no_selection_error))
        }
    }

    private fun initObservers() {
        viewModel.getRegisterMedia().observe(this, Observer { viewStatus ->
            viewStatus.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.LOADING -> {
                        mediaRegisterButton.isLoading(true)
                    }
                    ViewStateModel.Status.SUCCESS -> {
                        mediaRegisterButton.isLoading(false)
                        showMessageForTest(R.string.register_media_success_test)
                        activity?.finish()
                    }
                    ViewStateModel.Status.ERROR -> {
                        mediaRegisterButton.isLoading(false)
                        showError(it.errors)
                        showMessageForTest(R.string.register_media_generic_error_test)
                    }
                }
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun checkField(): Boolean =
        !TextUtils.isEmpty(selectedPlatform)
}