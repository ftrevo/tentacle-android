package br.com.concrete.tentacle.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R

class BaseAdapter<T>(
    var elements: ArrayList<T?> = ArrayList(),
    val layout: Int,
    private val holder: (mL: View) -> RecyclerView.ViewHolder,
    val holderCallback: (holder: RecyclerView.ViewHolder, T) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun getItemCount() = if (elements.isNullOrEmpty()) VIEW_TYPE_LOADING else elements.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProgressBarViewHolder -> holder
            else -> elements[position]?.let { holderCallback(holder, it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_LOADING) {
            ProgressBarViewHolder(
                inflater
                    .inflate(R.layout.item_progress, parent, false)
            )
        } else {
            holder(
                inflater
                    .inflate(layout, parent, false)
            )
        }
    }

    fun setNewList(elements: ArrayList<T?>) {
        this.elements = elements
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = if (elements[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
}