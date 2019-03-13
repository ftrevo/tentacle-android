package br.com.concrete.tentacle.features.library.loan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.game.Screenshot
import br.com.concrete.tentacle.data.models.library.Video

class LoanAdapter<T>(
    var elements: ArrayList<T> = ArrayList()
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SCREENSHOT = 0
    private val VIEW_TYPE_VIDEO = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_VIDEO) {
            VideoViewHolder(inflater.inflate(R.layout.item_game_video, parent, false))
        } else {
            ScreenshotsViewHolder(inflater.inflate(R.layout.item_game_screenshot, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is VideoViewHolder -> holder.bind(elements[position] as Video)
            is ScreenshotsViewHolder -> holder.bind(elements[position] as Screenshot)
        }
    }

    override fun getItemCount() = elements.size

    override fun getItemViewType(position: Int) = if (elements[position] is Video) VIEW_TYPE_VIDEO else VIEW_TYPE_SCREENSHOT
}