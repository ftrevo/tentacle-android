package br.com.concrete.tentacle.features.library

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.MediaLibrary
import kotlinx.android.synthetic.main.library_item_layout.view.*

class LibraryViewHolder(
    private val mLinearLayout: View
) : RecyclerView.ViewHolder(mLinearLayout) {

    companion object {
        private var viewStateOpen = false
        fun callBack(holder: RecyclerView.ViewHolder, element: Library) {
            if (holder is LibraryViewHolder) {
                holder.mLinearLayout.tvGameName.text = element.title

                holder.mLinearLayout.ivArrow.setOnClickListener {
                    if (viewStateOpen) animateClose(holder.mLinearLayout) else animateOpen(holder.mLinearLayout, element)
                    viewStateOpen = !viewStateOpen
                }
            }
        }

        private fun checkPlatform(view: View, list: List<MediaLibrary>) {
            view.visibility = if (list.isNotEmpty()) View.VISIBLE else View.GONE
        }

        private fun animateOpen(view: View, element: Library) {
            val anim = AnimationUtils.loadAnimation(view.context, R.anim.rotate_open)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                    showBullets(view, element)
                }
            })

            view.ivArrow.startAnimation(anim)
        }

        private fun animateClose(view: View) {
            val anim = AnimationUtils.loadAnimation(view.context, R.anim.rotate_close)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                    hideBullets(view)
                }
            })

            view.ivArrow.startAnimation(anim)
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
            view.tvNS.visibility = View.GONE
            view.tv3DS.visibility = View.GONE
            view.tvONE.visibility = View.GONE
            view.tv360.visibility = View.GONE
            view.tvPS4.visibility = View.GONE
            view.tvPS3.visibility = View.GONE
        }
    }
}