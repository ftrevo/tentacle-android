package br.com.concrete.tentacle.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter<T>(
    elements: ArrayList<T>,
    val layout: Int,
    val holder: (mL: View) -> RecyclerView.ViewHolder,
    val holderCallback: (holder: RecyclerView.ViewHolder, T) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = ArrayList<T>()

    init {
        list.addAll(elements)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holderCallback(holder, list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return holder(v)
    }

    fun updateList(newElements: ArrayList<T>) {
        list.clear()
        list.addAll(newElements)
        notifyDataSetChanged()
    }
}