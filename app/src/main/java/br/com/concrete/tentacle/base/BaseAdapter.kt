package br.com.concrete.tentacle.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R

class BaseAdapter<T>(
    var elements: ArrayList<T?> = ArrayList(),
    val layout: Int = -1,
    val holder: ((mL: View?) -> RecyclerView.ViewHolder?)? = null,
    val holderCallback: ((holder: RecyclerView.ViewHolder, T) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun getItemCount(): Int {
        return if (elements.isNullOrEmpty()) VIEW_TYPE_ITEM else elements.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProgressBarViewHolder -> holder.showLoading()
            else -> holderCallback?.let { it(holder, elements?.get(position)!!) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
}