package br.com.concrete.tentacle.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter<T>(
    var elements: ArrayList<T>,
    val layout: Int,
    val holder: (mL: View) -> RecyclerView.ViewHolder,
    val holderCallback: (holder: RecyclerView.ViewHolder, T) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return elements.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holderCallback(holder, elements[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return holder(v)
    }
}