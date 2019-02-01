package br.com.concrete.tentacle.util

import android.content.res.Resources
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class RecyclerPositionViewMatcher private constructor(private val recyclerViewId: Int) {

    private fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    private fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            internal var resources: Resources? = null
            internal var childView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = Integer.toString(recyclerViewId)
                if (this.resources != null) {
                    try {
                        idDescription = this.resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        idDescription = String.format("%s (resource printerName not found)", recyclerViewId)
                    }
                }

                description.appendText("with id: $idDescription")
            }

            public override fun matchesSafely(view: View): Boolean {

                this.resources = view.resources

                if (childView == null) {
                    val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        val childViewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                        if (childViewHolder != null)
                            childView = childViewHolder.itemView
                    } else {
                        return false
                    }
                }

                if (targetViewId == -1) {
                    return view === childView
                } else {
                    val targetView = childView!!.findViewById<View>(targetViewId)
                    return view === targetView
                }
            }
        }
    }

    companion object {

        fun withRecyclerView(@IdRes recyclerViewId: Int, atPosition: Int): Matcher<View> {
            return RecyclerPositionViewMatcher(recyclerViewId).atPosition(atPosition)
        }

        fun withRecyclerViewAndViewId(@IdRes recyclerViewId: Int, atPosition: Int, viewId: Int): Matcher<View> {
            return RecyclerPositionViewMatcher(recyclerViewId).atPositionOnView(atPosition, viewId)
        }
    }
}