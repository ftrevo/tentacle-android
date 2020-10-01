package br.com.concrete.tentacle.features.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.matchers.RecyclerViewMatcher.Companion.withRecyclerView
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not

fun arrange(action: HomeArrange.() -> Unit) {
    HomeArrange().apply(action)
}

fun act(action: HomeAct.() -> Unit) {
    HomeAct().apply(action)
}

fun assert(action: HomeAssert.() -> Unit) {
    HomeAssert().apply(action)
}

class HomeArrange {

    fun mockLoadHomeGames(mockWebServer: MockWebServer) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/home/load_home_games_success_empty.json".getJson())
        )
    }

    fun mockNewHomeGamesResponse(mockWebServer: MockWebServer) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/home/new_home_games_success.json".getJson())
        )
    }

    fun mockErrorResponse(mockWebServer: MockWebServer, error: Int = 400) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(error)
                .setBody("mockjson/errors/error_400.json".getJson())
        )
    }

    fun mockUpdateErrorResponse(mockWebServer: MockWebServer) {
        mockErrorResponse(mockWebServer, 426)
    }
}

class HomeAct {

    fun clickErrorButton() {
        onView(withId(R.id.buttonNameError)).perform(click())
    }
}

class HomeAssert {

    fun isErrorMessageDisplayed() {
        isDisplayed("ERROR MESSAGE.")
    }

    fun isLoadAgainMessageDisplayed() {
        isDisplayed("Carregar Novamente")
    }

    fun isListDisplayed() {
        isDisplayed(R.id.recyclerListView)
    }

    fun isListNotDisplayed() {
        isNotDisplayed(R.id.recyclerListView)
    }

    fun isListErrorDisplayed() {
        isDisplayed(R.id.recyclerListError)
    }

    fun isListErrorNotDisplayed() {
        isNotDisplayed(R.id.recyclerListError)
    }

    fun isRegisterButtonDisplayedProperly() {
        onView(withId(R.id.buttonNameError))
            .check(matches(hasDescendant(withText("Adicionar novo jogo"))))
    }

    private fun hasText(resId: Int, text: String) {
        onView(withId(resId))
            .check(matches(withText(text)))
    }

    fun checkNoRegisteredGameErrorDescription() {
        hasText(
            R.id.errorDescription,
            "A Home ainda não possui jogos novos cadastrados. Comece a cadastrar para eles aparecerem aqui!"
        )
    }

    fun checkGenericErrorDescription() {
        hasText(
            R.id.errorDescription,
            "Ocorreu um erro ao carregar os jogos. Por favor, tente novamente."
        )
    }

    fun checkListItemsTitle() {
        getGodOfWarElement()
            .check(matches(hasDescendant(withText("God of War III"))))
        getTheLastOfUsElement()
            .check(matches(hasDescendant(withText("The Last of Us Remastered"))))
    }

    fun checkListItemSummary() {
        getGodOfWarElement()
            .check(
                matches(
                    hasDescendant(
                        allOf(
                            withId(R.id.homeItemDescriptionTextView),
                            withText("is an action-adventure game developed by Santa Monica Studio and published by Sony Computer Entertainment (SCE). Released for the PlayStation 3 (PS3) console on March 16, 2010, the game is the fifth installment in the God of War series, the seventh chronologically, and the sequel to 2007's God of War II. Loosely based on Greek mythology, the game is set in ancient Greece with vengeance as its central motif. The player controls the protagonist and former God of War Kratos, after his betrayal at the hands of his father Zeus, King of the Olympian gods. Reigniting the Great War, Kratos ascends Mount Olympus until he is abandoned by the Titan Gaia. Guided by Athena’s spirit, Kratos battles monsters, gods, and Titans in a search for Pandora, without whom he cannot open Pandora's Box, defeat Zeus, and end the reign of the Olympian gods.")
                        )
                    )
                )
            )
    }

    fun checkListItemsPlatforms() {
        val lastOfUs = getTheLastOfUsElement()
        isTextDisplayedInsideListItem(lastOfUs, "PS4")
        isTextNotDisplayedInsideListItem(lastOfUs, "PS3")
        isTextNotDisplayedInsideListItem(lastOfUs, "360")
        isTextNotDisplayedInsideListItem(lastOfUs, "ONE")
        isTextNotDisplayedInsideListItem(lastOfUs, "3DS")
        isTextNotDisplayedInsideListItem(lastOfUs, "NS")

        val godOfWar = getGodOfWarElement()
        isTextDisplayedInsideListItem(godOfWar, "360")
        isTextDisplayedInsideListItem(godOfWar, "ONE")
        isTextNotDisplayedInsideListItem(godOfWar, "PS4")
        isTextNotDisplayedInsideListItem(godOfWar, "PS3")
        isTextNotDisplayedInsideListItem(godOfWar, "3DS")
        isTextNotDisplayedInsideListItem(godOfWar, "NS")
    }

    private fun isTextDisplayedInsideListItem(viewInteraction: ViewInteraction, text: String) {
        viewInteraction
            .check(matches(hasDescendant(allOf(withText(text), isDisplayed()))))
    }

    private fun isTextNotDisplayedInsideListItem(itemInteraction: ViewInteraction, text: String) {
        itemInteraction
            .check(matches(hasDescendant(allOf(withText(text), not(isDisplayed())))))
    }

    private fun getTheLastOfUsElement() = onView(
        withRecyclerView(R.id.recyclerListView).atPosition(0)
    )

    private fun getGodOfWarElement() = onView(
        withRecyclerView(R.id.recyclerListView).atPosition(1)
    )

    private fun isDisplayed(resId: Int) {
        onView(withId(resId)).check(
            matches(
                isDisplayed()
            )
        )
    }

    private fun isDisplayed(text: String) {
        onView(withText(text)).check(
            matches(
                isDisplayed()
            )
        )
    }

    private fun isNotDisplayed(resId: Int) {
        onView(withId(resId)).check(matches(not(isDisplayed())))
    }
}