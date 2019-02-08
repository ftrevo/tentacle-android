package br.com.concrete.tentacle.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_progress.view.itemProgress

class ProgressBarViewHolder(val item: View) : RecyclerView.ViewHolder(item) {

    fun showLoading() {
        val visibility = item.itemProgress.visibility
        when (visibility) {
            View.GONE -> item.itemProgress.visibility = View.VISIBLE
            View.VISIBLE -> item.itemProgress.visibility = View.GONE
        }
    }
}