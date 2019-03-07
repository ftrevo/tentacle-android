package br.com.concrete.tentacle.features.registerGame.registerMedia

import android.content.DialogInterface
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
import br.com.concrete.tentacle.utils.DialogUtils
import br.com.concrete.tentacle.utils.EMPTY_STRING
import br.com.concrete.tentacle.utils.PLATFORM_PS3_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_PS4_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_360
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_ONE
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_3DS
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_SWITCH
import kotlinx.android.synthetic.main.fragment_register_media.*
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterMediaFragment : BaseFragment() {

    private val viewModel: RegisterMediaViewModel by viewModel()
    private lateinit var idGame: String
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
        mediaRegisterButton.disable()

        arguments?.let { bundle ->
            idGame = RegisterMediaFragmentArgs.fromBundle(bundle).gameArgument
            viewModel.getDetailsGame(idGame)

            initListeners()
            initObservers()
        }
    }

    private fun initListeners() {
        mediaRegisterButton.setOnClickListener {
            if (checkField()) {
                context?.let { context ->
                    DialogUtils.showDialog(
                        context = context,
                        title = getString(R.string.toolbar_title_search_game),
                        message = String.format(getString(R.string.confirm_game), game.name, selectedPlatform),
                        positiveText = getString(R.string.confirm),
                        negativeText = getString(R.string.back),
                        positiveListener = DialogInterface.OnClickListener { _, _ ->
                            viewModel.registerMedia(selectedPlatform, game)
                        }
                    )
                }
            } else context?.callSnackbar(view!!, getString(R.string.register_media_no_selection_error))
        }

        container.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipPs3 -> {
                    selectedPlatform = PLATFORM_PS3_ABBREV
                    mediaRegisterButton.enable()
                }
                R.id.chipPs4 -> {
                    selectedPlatform = PLATFORM_PS4_ABBREV
                    mediaRegisterButton.enable()
                }
                R.id.chip360 -> {
                    selectedPlatform = PLATFORM_XBOX_360
                    mediaRegisterButton.enable()
                }
                R.id.chipOne -> {
                    selectedPlatform = PLATFORM_XBOX_ONE
                    mediaRegisterButton.enable()
                }
                R.id.chip3ds -> {
                    selectedPlatform = PLATFORM_NINTENDO_3DS
                    mediaRegisterButton.enable()
                }
                R.id.chipSwitch -> {
                    selectedPlatform = PLATFORM_NINTENDO_SWITCH
                    mediaRegisterButton.enable()
                }
                else -> {
                    mediaRegisterButton.disable()
                }
            }
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
                        showError(it.errors, getString(R.string.game_already_registered))
                        showMessageForTest(R.string.register_media_generic_error_test)
                    }
                }
            }
        })

        viewModel.getDetailGame().observe(this, Observer { event ->
            event.getContentIfNotHandler()?.let { viewState ->
                when (viewState.status) {
                    ViewStateModel.Status.LOADING -> {
                        mediaRegisterButton.isLoading(true)
                    }
                    ViewStateModel.Status.SUCCESS -> {
                        mediaRegisterButton.isLoading(false)

                        viewState.model?.let {
                            gameView.setGame(it)
                            game = it
                        }
                    }
                    ViewStateModel.Status.ERROR -> {
                        mediaRegisterButton.isLoading(false)
                        showError(viewState.errors)
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