package br.com.concrete.tentacle.features.library

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.MediaLibrary
import br.com.concrete.tentacle.data.models.library.filter.SubItem
import br.com.concrete.tentacle.extensions.animation
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_3DS
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_SWITCH
import br.com.concrete.tentacle.utils.PLATFORM_PS3_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_PS4_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_360
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_ONE
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
        fun callBack(holder: RecyclerView.ViewHolder, element: Library, selectedFilters: List<SubItem>) {
            if (holder is LibraryViewHolder) {
                holder.mLinearLayout.tvGameName.text = element.name
                holder.mLinearLayout.ivArrow.setOnClickListener {
                    if (holder.viewStateOpen) animateClose(holder.mLinearLayout)
                    else animateOpen(holder.mLinearLayout, element, selectedFilters)
                    holder.viewStateOpen = !holder.viewStateOpen
                }
                holder.itemView.setOnClickListener {
                    holder.listener(element)
                }
            }
        }

        private fun checkPlatform(view: View, hasMedia: Int, filter: SubItem?, hasAnyFilters: Boolean) {
            view.visibility = if (hasMedia > 0) View.VISIBLE else View.GONE

            if (hasAnyFilters) {
                view.visibility = if (filter != null) View.VISIBLE else View.GONE
            }
        }


        private fun animateOpen(view: View, element: Library, selectedFilters: List<SubItem>) {
            view.ivArrow.animation(R.anim.rotate_open) {
                showBullets(view, element, selectedFilters)
            }
        }

        private fun animateClose(view: View) {
            view.ivArrow.animation(R.anim.rotate_close) {
                hideBullets(view)
            }
        }

        private fun showBullets(view: View, element: Library, selectedFilters: List<SubItem>) {
            view.groupLayout.visibility = View.VISIBLE

            val hasAnyFiltersSelected = selectedFilters.isNotEmpty()
            checkPlatform(
                view.tv360,
                element.mediaXbox360Count,
                selectedFilters.firstOrNull { subItem -> subItem.queryParameter == PLATFORM_XBOX_360 },
                hasAnyFiltersSelected)
            checkPlatform(
                view.tv3DS,
                element.mediaNintendo3dsCount,
                selectedFilters.firstOrNull { subItem -> subItem.queryParameter == PLATFORM_NINTENDO_3DS },
                hasAnyFiltersSelected)
            checkPlatform(
                view.tvNS,
                element.mediaNintendoSwitchCount,
                selectedFilters.firstOrNull { subItem -> subItem.queryParameter == PLATFORM_NINTENDO_SWITCH },
                hasAnyFiltersSelected)
            checkPlatform(
                view.tvONE,
                element.mediaXboxOneCount,
                selectedFilters.firstOrNull { subItem -> subItem.queryParameter == PLATFORM_XBOX_ONE },
                hasAnyFiltersSelected)
            checkPlatform(
                view.tvPS3,
                element.mediaPs3Count,
                selectedFilters.firstOrNull { subItem -> subItem.queryParameter == PLATFORM_PS3_ABBREV },
                hasAnyFiltersSelected)
            checkPlatform(
                view.tvPS4,
                element.mediaPs4Count,
                selectedFilters.firstOrNull { subItem -> subItem.queryParameter == PLATFORM_PS4_ABBREV },
                hasAnyFiltersSelected)
        }

        private fun hideBullets(view: View) {
            view.groupLayout.visibility = View.GONE
        }
    }
}