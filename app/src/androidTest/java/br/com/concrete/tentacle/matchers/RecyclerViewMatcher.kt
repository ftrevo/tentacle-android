package br.com.concrete.tentacle.matchers

import android.content.res.Resources
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description

class RecyclerViewMatcher(private val recyclerViewId: Int) {

    companion object {
        fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher =
                RecyclerViewMatcher(recyclerViewId)
    }

    fun atPosition(position: Int): Matcher<View> = atPositionOnView(position, -1)

    private fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

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
}