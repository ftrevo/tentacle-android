package br.com.concrete.tentacle.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.getPartOfDate
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.extensions.visible
import kotlinx.android.synthetic.main.game_view_header_layout.view.rbGame
import kotlinx.android.synthetic.main.game_view_header_layout.view.tvGameName
import kotlinx.android.synthetic.main.game_view_header_layout.view.tvGameReleaseYear
import kotlinx.android.synthetic.main.game_view_layout.view.gameModeContainer
import kotlinx.android.synthetic.main.game_view_layout.view.genresContainer
import kotlinx.android.synthetic.main.game_view_layout.view.groupStatus
import kotlinx.android.synthetic.main.game_view_layout.view.tvGameSummary
import java.util.Calendar

class GameView(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {
    init {
        View.inflate(context, R.layout.game_view_layout, this)
    }

    fun setGame(game: Game) {

        with(game) {

            // TODO usar o Glide para pegar o cover
            // ivGameCover

            val inflater = LayoutInflater.from(this@GameView.context)

            tvGameName.text = name
            rating?.let {
                rbGame.progress = Math.round(it)
            }
            releaseDate?.let {
                tvGameReleaseYear.text = it.toDate().getPartOfDate(Calendar.YEAR)
            }

            summary?.let {
                tvGameSummary.text = it
            }

            gameModes?.let {
                gameModeContainer.removeAllViews()
                it.forEach { gameMode ->
                    val tv = inflater.inflate(R.layout.game_mode_text_layout, gameModeContainer) as TextView
                    gameMode.name?.let { name ->
                        tv.text = name
                        gameModeContainer.addView(tv)
                    }
                }
            }

            genres?.let {
                genresContainer.removeAllViews()
                it.forEach { genre ->
                    val tv = inflater.inflate(R.layout.genre_chip_layout, gameModeContainer) as TextView
                    genre.name?.let { name ->
                        tv.text = name
                        genresContainer.addView(tv)
                    }
                }
            }
        }
    }

    fun showStatusView(show: Boolean){
        groupStatus.visible(show)
    }
}