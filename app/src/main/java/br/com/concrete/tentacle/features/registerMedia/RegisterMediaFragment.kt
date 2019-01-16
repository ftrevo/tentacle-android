package br.com.concrete.tentacle.features.registerMedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.callSnackbar
import br.com.concrete.tentacle.utils.ARGUMENT_GAME
import kotlinx.android.synthetic.main.fragment_register_media.*

class RegisterMediaFragment: Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_register_media, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val game = arguments?.getParcelable<Game>(ARGUMENT_GAME)

        setupViews(game)

        initListeners()
    }

    private fun setupViews(game: Game?) {
        game?.let {
            mediaNameTextView.text = game.title
        }
    }

    private fun initListeners() {
        mediaRegisterButton.setOnClickListener {
            if (checkField()) /* TODO - register media */
            else context?.callSnackbar(view!!, "Necessário escolher a plataforma do jogo")
        }
    }

    private fun checkField(): Boolean =
        when(mediaPlataformRadioGroup.checkedRadioButtonId) {
            mediaPS4RadioButton.id, mediaPS3RadioButton.id, mediaXbox360RadioButton.id,
            mediaXboxOneRadioButton.id, mediaPS3RadioButton.id, mediaSwitchRadioButton.id -> true
            else ->false
        }



}