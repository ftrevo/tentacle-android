package br.com.concrete.tentacle.features.registerGame.remoteGame

import android.os.Bundle
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withResourceName
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.getPartOfDate
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.matchers.RecyclerViewMatcher.Companion.withRecyclerView
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Test
import java.util.Calendar

class RemoteGameFragmentTest : BaseFragmentTest() {

    override fun setupFragment() {
        testFragment = RemoteGameFragment().apply {
            val args = Bundle()
            args.putSerializable("game_name", "Nome do jogo")
            arguments = args
        }
    }

    @Test
    fun shouldShowProperTitleToobar() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/registerMedia/game.json".getJson())
        )
        onView(allOf(
            instanceOf(TextView::class.java),
            withParent(withResourceName("action_bar"))))
            .check(matches(withText("Nome do jogo")))
    }

    @Test
    fun showEmptyErrorCustomLayout() {
        val response = "mockjson/common/common_empty_list_success.json".getJson()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )

        onView(withId(R.id.recyclerListError))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.errorDescription))
            .check(ViewAssertions.matches(withText("Jogo não encontrado")))
        onView(withId(R.id.progressBarList))
            .check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun showRecyclerViewWithItems() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/registerMedia/remote_games_success.json".getJson())
        )

        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun shouldShowItemsContentProperly() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/registerMedia/remote_games_success.json".getJson())
        )

        val gameName = "Super Mario 3D Land"
        val releaseDate = "2011-11-03T00:00:00.000Z".toDate().getPartOfDate(Calendar.YEAR)

        onView(withRecyclerView(R.id.recyclerListView).atPosition(2))
            .check(matches(hasDescendant(withText(gameName))))
            .check(matches(hasDescendant(withText(releaseDate))))
    }
}