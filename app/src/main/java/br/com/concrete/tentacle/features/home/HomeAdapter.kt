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
        return imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = LayoutInflater.from(container.context).inflate(R.layout.home_image_view, null) as ImageView

        imageView.loadImageUrl(
            Utils.assembleGameImageUrl(
                sizeType = IMAGE_SIZE_TYPE_SCREENSHOT_HUGE,
                imageId = imageList[position]
            )
        )

        container.addView(imageView,0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
//        super.destroyItem(container, position, view)
        container.removeView(view as View)
    }
}