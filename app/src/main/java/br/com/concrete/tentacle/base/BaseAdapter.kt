package br.com.concrete.tentacle.base

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter<T>(
    var elements: List<T>,
    val item: Int,
    val holder: (mL: LinearLayout) -> RecyclerView.ViewHolder,
    val holderCallback: (holder: RecyclerView.ViewHolder, position: Int) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int {
        return elements.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holderCallback(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(item, parent, false) as LinearLayout

        return holder(v)
    }

}