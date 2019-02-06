package br.com.concrete.tentacle.features.library

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.MediaLibrary
import br.com.concrete.tentacle.extensions.animation
import kotlinx.android.synthetic.main.library_item_layout.view.group
import kotlinx.android.synthetic.main.library_item_layout.view.ivArrow
import kotlinx.android.synthetic.main.library_item_layout.view.tv360
import kotlinx.android.synthetic.main.library_item_layout.view.tv3DS
import kotlinx.android.synthetic.main.library_item_layout.view.tvGameName
import kotlinx.android.synthetic.main.library_item_layout.view.tvNS
import kotlinx.android.synthetic.main.library_item_layout.view.tvONE
import kotlinx.android.synthetic.main.library_item_layout.view.tvPS3
import kotlinx.android.synthetic.main.library_item_layout.view.tvPS4

class LibraryViewHolder(
    private val mLinearLayout: View
) : RecyclerView.ViewHolder(mLinearLayout) {

    companion object {
        private var viewStateOpen = false

        fun callBack(holder: RecyclerView.ViewHolder, element: Library) {
            if (holder is LibraryViewHolder) {
                holder.mLinearLayout.tvGameName.text = element.title

                holder.mLinearLayout.ivArrow.setOnClickListener {
                    if (this.viewStateOpen) animateClose(holder.mLinearLayout) else animateOpen(holder.mLinearLayout, element)
                    viewStateOpen = !viewStateOpen
                }
            }
        }

        private fun checkPlatform(view: View, list: List<MediaLibrary>) {
            view.visibility = if (list.isNotEmpty()) View.VISIBLE else View.GONE
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
            checkPlatform(view.tv360, element.mediaXbox360)
            checkPlatform(view.tv3DS, element.mediaNintendo3ds)
            checkPlatform(view.tvNS, element.mediaNintendoSwitch)
            checkPlatform(view.tvONE, element.mediaXboxOne)
            checkPlatform(view.tvPS3, element.mediaPs3)
            checkPlatform(view.tvPS4, element.mediaPs4)
        }

        private fun hideBullets(view: View) {
            view.group.visibility = View.GONE
        }
    }
}