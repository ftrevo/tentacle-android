package br.com.concrete.tentacle.features.library

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.extensions.animation
import kotlinx.android.synthetic.main.library_item_layout.view.groupLayout
import kotlinx.android.synthetic.main.library_item_layout.view.ivArrow
import kotlinx.android.synthetic.main.library_item_layout.view.tv360
import kotlinx.android.synthetic.main.library_item_layout.view.tv3DS
import kotlinx.android.synthetic.main.library_item_layout.view.tvGameName
import kotlinx.android.synthetic.main.library_item_layout.view.tvNS
import kotlinx.android.synthetic.main.library_item_layout.view.tvONE
import kotlinx.android.synthetic.main.library_item_layout.view.tvPS3
import kotlinx.android.synthetic.main.library_item_layout.view.tvPS4

class LibraryViewHolder(
    private val mLinearLayout: View,
    var viewStateOpen: Boolean = false,
    val listener: (library: Library) -> Unit
) : RecyclerView.ViewHolder(mLinearLayout) {

    companion object {
        fun callBack(holder: RecyclerView.ViewHolder, element: Library) {
            if (holder is LibraryViewHolder) {
                holder.mLinearLayout.tvGameName.text = element.name
                holder.mLinearLayout.ivArrow.setOnClickListener {
                    if (holder.viewStateOpen) animateClose(holder.mLinearLayout) else animateOpen(holder.mLinearLayout, element)
                    holder.viewStateOpen = !holder.viewStateOpen
                }
                holder.itemView.setOnClickListener {
                    holder.listener(element)
                }
            }
        }

        private fun checkPlatform(view: View, hasMedia: Int) {
            view.visibility = if (hasMedia > 0) View.VISIBLE else View.GONE
        }

        private fun animateOpen(view: View, element: Library) {
            view.ivArrow.animation(R.anim.rotate_open) {
                showBullets(view, element)
            }
        }

        private fun animateClose(view: View) {
            view.ivArrow.animation(R.anim.rotate_close) {
                hideBullets(view)
            }
        }

        private fun showBullets(view: View, element: Library) {
            view.groupLayout.visibility = View.VISIBLE
            checkPlatform(view.tv360, element.mediaXbox360Count)
            checkPlatform(view.tv3DS, element.mediaNintendo3dsCount)
            checkPlatform(view.tvNS, element.mediaNintendoSwitchCount)
            checkPlatform(view.tvONE, element.mediaXboxOneCount)
            checkPlatform(view.tvPS3, element.mediaPs3Count)
            checkPlatform(view.tvPS4, element.mediaPs4Count)
        }

        private fun hideBullets(view: View) {
            view.groupLayout.visibility = View.GONE
        }
    }
}