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
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.extensions.progress
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.extensions.visible
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_COVER_SMALL
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.game_view_header_layout.view.ivGameCover
import kotlinx.android.synthetic.main.game_view_header_layout.view.rbGame
import kotlinx.android.synthetic.main.game_view_header_layout.view.tvGameName
import kotlinx.android.synthetic.main.game_view_header_layout.view.tvGameReleaseYear
import kotlinx.android.synthetic.main.game_view_header_layout.view.groupStatus
import kotlinx.android.synthetic.main.game_view_layout.view.*
import java.util.*

class GameView(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {
    init {
        View.inflate(context, R.layout.game_view_layout, this)
    }

    fun setGame(game: Game) {

        with(game) {

            cover?.imageId?.let {
                ivGameCover.loadImageUrl(Utils.assembleGameImageUrl(IMAGE_SIZE_TYPE_COVER_SMALL, it))
            }

            val inflater = LayoutInflater.from(this@GameView.context)

            tvGameName.text = name
            rating?.let {
                rbGame.progress(it)
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
                    val tv = inflater.inflate(R.layout.game_mode_text_layout, null) as TextView
                    gameMode.name?.let { name ->
                        tv.text = name
                        gameMode.getIconResource()?.let { icon ->
                            tv.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
                        }
                        gameModeContainer.addView(tv)
                    }
                }
            }

            genres?.let {
                genresContainer.removeAllViews()
                it.forEach { genre ->
                    val tv = inflater.inflate(R.layout.genre_chip_layout, null) as TextView
                    genre.name?.let { name ->
                        tv.text = name
                        genresContainer.addView(tv)
                    }
                }
            }
        }
    }

    fun showStatusView(show: Boolean) {
        groupStatus.visible(show)
    }
}