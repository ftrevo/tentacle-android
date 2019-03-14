package br.com.concrete.tentacle.features.library.loan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R

class LoanAdapter<T>(
    var elements: ArrayList<T> = ArrayList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LoanViewHolder(inflater.inflate(R.layout.item_game_video, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LoanViewHolder -> holder.bind(elements[position] as Any)
        }
    }

    override fun getItemCount() = elements.size
}