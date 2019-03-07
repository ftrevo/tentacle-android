package br.com.concrete.tentacle.extensions

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

fun Int.checkLines(): TypeSafeMatcher<View> {

    var lines = this
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("IsTextInLines")
        }

        override fun matchesSafely(item: View?): Boolean {
            return (item as TextView).lineCount == lines
        }
    }
}

fun Int.atPositionOnView(position: Int, targetViewId: Int = -1): TypeSafeMatcher<View> {
    val recyclerViewId = this
    return object : TypeSafeMatcher<View>() {
        var resources: Resources? = null
        var childView: View? = null

        override fun describeTo(description: Description) {
            var idDescription = Integer.toString(recyclerViewId)
            if (this.resources != null) {
                try {
                    idDescription = this.resources!!.getResourceName(recyclerViewId)
                } catch (var4: Resources.NotFoundException) {
                    idDescription = String.format(
                        "%s (resource name not found)",
                        *arrayOf<Any>(Integer.valueOf(recyclerViewId))
                    )
                }
            }

            description.appendText("with id: $idDescription")
        }

        public override fun matchesSafely(view: View): Boolean {

            this.resources = view.resources

            if (childView == null) {
                val recyclerView = view.rootView.findViewById(recyclerViewId) as RecyclerView?
                if (recyclerView != null && recyclerView.id == recyclerViewId) {
                    childView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                } else {
                    return false
                }
            }

            return if (targetViewId == -1) {
                view === childView
            } else {
                val targetView = childView!!.findViewById<View>(targetViewId)
                view === targetView
            }
        }
    }
}
