package br.com.concrete.tentacle.features.library

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.library.Library
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
                    if(viewStateOpen) animateClose(holder.mLinearLayout) else animateOpen(holder.mLinearLayout)
                    viewStateOpen = !viewStateOpen
                }
            }
        }

        private fun animateOpen(view: View){
            val anim = AnimationUtils.loadAnimation(view.context, R.anim.rotate_open)
            anim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {

                }

                override fun onAnimationStart(animation: Animation?) {
                    showBullets(view, true)
                }

            })

            view.ivArrow.startAnimation(anim)
        }

        private fun animateClose(view: View){
            val anim = AnimationUtils.loadAnimation(view.context, R.anim.rotate_close)
            anim.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {

                }

                override fun onAnimationStart(animation: Animation?) {
                    showBullets(view,false)
                }

            })

            view.ivArrow.startAnimation(anim)
        }

        private fun showBullets(view: View, show: Boolean){
            view.tvNS.visibility = if(show) View.VISIBLE else View.GONE
            view.tv3DS.visibility = if(show) View.VISIBLE else View.GONE
            view.tvONE.visibility = if(show) View.VISIBLE else View.GONE
            view.tv360.visibility = if(show) View.VISIBLE else View.GONE
            view.tvPS4.visibility = if(show) View.VISIBLE else View.GONE
            view.tvPS3.visibility = if(show) View.VISIBLE else View.GONE
        }
    }
}