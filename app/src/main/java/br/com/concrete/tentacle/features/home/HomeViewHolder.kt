package br.com.concrete.tentacle.features.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.progress
import br.com.concrete.tentacle.extensions.visible
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_home_game.view.homeItemBackground
import kotlinx.android.synthetic.main.item_home_game.view.homeItemDescriptionTextView
import kotlinx.android.synthetic.main.item_home_game.view.homeItemGameRatinBar
import kotlinx.android.synthetic.main.item_home_game.view.homeItemTitleTextView
import kotlinx.android.synthetic.main.item_home_game.view.tv360
import kotlinx.android.synthetic.main.item_home_game.view.tv3DS
import kotlinx.android.synthetic.main.item_home_game.view.tvNS
import kotlinx.android.synthetic.main.item_home_game.view.tvONE
import kotlinx.android.synthetic.main.item_home_game.view.tvPS3
import kotlinx.android.synthetic.main.item_home_game.view.tvPS4


class HomeViewHolder(
    private val mLinearLayout: View,
    private val observable: Observable<Boolean>,
    private var disposable: Disposable? = null
) : RecyclerView.ViewHolder(mLinearLayout) {

    companion object {
        private val SCROLL_DURATION_FACTOR = 10.0

        fun callBack(holder: RecyclerView.ViewHolder, element: Game, listener: (Game) -> Unit) {
            if (holder is HomeViewHolder) {

                holder.mLinearLayout.let { itemView ->
                    val ids = element.screenshots?.map {
                        it.imageId
                    }
                    ids?.let {
                        val homeAdapter = HomeAdapter(it)
                        var jumpPosition = -1

                        val onPageChange  = object : ViewPager.SimpleOnPageChangeListener() {
                            override fun onPageScrollStateChanged(state: Int) {
                                if (jumpPosition >= 0) {
                                    itemView.homeItemBackground.setCurrentItem(jumpPosition, false)
                                    jumpPosition = -1
                                }
                            }

                            override fun onPageSelected(position: Int) {
                                if (position == 0) {
                                    jumpPosition = homeAdapter.getRealCount()
                                } else if (position == homeAdapter.getRealCount() + 1) {
                                    jumpPosition = 1
                                }
                            }
                        }

                        itemView.homeItemBackground.addOnPageChangeListener(onPageChange)

                        holder.disposable?.dispose()

                        holder.disposable = holder.observable.subscribe {
                            itemView.homeItemBackground.nextPage()
                        }

                        itemView.homeItemBackground.adapter = homeAdapter
                        itemView.homeItemBackground.setScrollDurationFactor(SCROLL_DURATION_FACTOR)
                        itemView.homeItemBackground.setCurrentItem(1, false)

                        itemView.homeItemTitleTextView.text = element.name
                        element.summary?.let { description ->
                            itemView.homeItemDescriptionTextView.text = description
                        }
                        element.rating?.let { rating ->
                            itemView.homeItemGameRatinBar.progress(rating)
                        }

                        itemView.tv360.visible(element.mediaXbox360Count > 0)
                        itemView.tv3DS.visible(element.mediaNintendo3dsCount > 0)
                        itemView.tvNS.visible(element.mediaNintendoSwitchCount > 0)
                        itemView.tvONE.visible(element.mediaXboxOneCount > 0)
                        itemView.tvPS3.visible(element.mediaPs3Count > 0)
                        itemView.tvPS4.visible(element.mediaPs4Count > 0)

                        itemView.setOnClickListener {
                            listener(element)
                        }
                    }
                }
            }
        }
    }
}