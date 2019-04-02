package br.com.concrete.tentacle.features.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_SCREENSHOT_HUGE
import br.com.concrete.tentacle.utils.Utils

class HomeAdapter(private val imageList: List<String>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return if (imageList.isEmpty()) 0 else imageList.size + 2
    }

    fun getRealCount(): Int {
        return imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = LayoutInflater.from(container.context).inflate(R.layout.home_image_view, null) as ImageView

        val modelPosition = mapPagerPositionToModelPosition(position
        )
        imageView.loadImageUrl(
            Utils.assembleGameImageUrl(
                sizeType = IMAGE_SIZE_TYPE_SCREENSHOT_HUGE,
                imageId = imageList[modelPosition]
            )
        )

        container.addView(imageView, 0)
        return imageView
    }

    private fun mapPagerPositionToModelPosition(pagerPosition: Int): Int {
        if (pagerPosition == 0) {
            return getRealCount() - 1
        }

        if (pagerPosition == getRealCount() + 1) {
            return 0
        }
        return pagerPosition - 1
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}