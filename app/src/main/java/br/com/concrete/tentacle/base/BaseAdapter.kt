package br.com.concrete.tentacle.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R

class BaseAdapter<T>(
    elements: ArrayList<T>,
    val layout: Int,
    val holder: (mL: View) -> RecyclerView.ViewHolder,
    val holderCallback: (holder: RecyclerView.ViewHolder, T) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = ArrayList<T>()
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    init {
        list.addAll(elements)
    }


    override fun getItemCount(): Int {
        return list.size
        return if (elements.isNullOrEmpty()) VIEW_TYPE_ITEM else elements.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holderCallback(holder, list[position])
        when (holder) {
            is ProgressBarViewHolder -> holder.showLoading()
            else -> holderCallback?.let { it(holder, list?.get(position)!!) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return holder(v)
        return if (viewType == VIEW_TYPE_LOADING) {
            ProgressBarViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_progress, parent, false)
            )
        } else {
            holder?.let {
                it(
                    LayoutInflater.from(parent.context)
                        .inflate(layout, parent, false)
                )
            }!!
        }
    }

    fun setNewList(elements: ArrayList<T?>) {
        this.elements = elements
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (elements[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun updateList(newElements: ArrayList<T>) {
        list.clear()
        list.addAll(newElements)
        notifyDataSetChanged()
    }
}