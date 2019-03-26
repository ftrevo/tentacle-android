package br.com.concrete.tentacle.features.myreservations

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.waitUntil
import br.com.concrete.tentacle.matchers.RecyclerViewMatcher.Companion.withRecyclerView
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class MyReservationFragmentTest : BaseFragmentTest() {
    override fun setupFragment() {
        testFragment = MyReservationFragment()
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
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.errorDescription))
            .check(matches(withText("Você ainda não possui nenhum pedido de reserva.")))
        onView(withId(R.id.progressBarList))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun showRecycleViewWithItems() {
        val response = "mockjson/myreservations/load_my_reservations_success.json".getJson()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun showRecycleViewWithItemsExpired() {
        val response = "mockjson/myreservations/load_my_reservations_success.json".getJson()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(not(isDisplayed())))
        onView(withRecyclerView(R.id.recyclerListView)
            .atPosition(0))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(withText("Eeee"))))
            .check(matches(hasDescendant(withText("07/03/19"))))
            .check(matches(hasDescendant(withText("Dono: YARA"))))
    }

    @Test
    fun showRecycleViewWithItemsPendente() {
        val response = "mockjson/myreservations/load_my_reservations_success.json".getJson()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(not(isDisplayed())))
        onView(withRecyclerView(R.id.recyclerListView)
            .atPosition(2))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(withText("o jogo da vida"))))
            .check(matches(hasDescendant(withText("Pendente"))))
            .check(matches(hasDescendant(withText("Dono: DAIVID V. LEAL"))))
    }

    @Test
    fun showErrorMessageAndButtonLoadAgain() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody("mockjson/errors/error_400.json".getJson())
                .setResponseCode(400)
        )

        onView(withId(R.id.recyclerListView))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.progressBarList))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerListError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.errorDescription))
            .check(matches(withText("Ocorreu um erro ao carregas as reservas. Por favor, tente novamente.")))
    }

    @Test
    fun showErrorMessageAndClickButtonLoadAgain() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody("mockjson/errors/error_400.json".getJson())
                .setResponseCode(400)
        )

        val response = "mockjson/myreservations/load_my_reservations_success.json".getJson()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )

        onView(withId(R.id.recyclerListError))
            .perform(isDisplayed().waitUntil())
        onView(withId(R.id.errorDescription))
            .check(matches(withText("Ocorreu um erro ao carregas as reservas. Por favor, tente novamente.")))

        onView(withText(R.string.load_again))
            .perform(click())

        onView(withId(R.id.recyclerListView))
            .perform(isDisplayed().waitUntil())
    }

    @Test
    fun longClickDeleteExcluir() {
        val response = "mockjson/myreservations/load_my_reservations_success.json".getJson()
        val removeGame = "mockjson/myreservations/load_my_reservations_success_after_remove.json".getJson()
        setResponse(response, 200)
        setResponse(removeGame, 200)

        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(not(isDisplayed())))
        onView(withRecyclerView(R.id.recyclerListView)
            .atPosition(0))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(withText("Eeee"))))
        onView(
            withRecyclerView(R.id.recyclerListView)
                .atPosition(0))
            .check(matches(isDisplayed()))
            .perform(ViewActions.longClick())

        onView(withText("A reserva do jogo Eeee sairá da lista de \"Minhas Reservas\", mas você poderá adicioná-lo novamente no futuro."))

        onView(withText("REMOVER"))
            .check(matches(isDisplayed()))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(ViewActions.click())

        onView(withRecyclerView(R.id.recyclerListView)
            .atPosition(0))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(not(withText("Eeee")))))
    }

    @Test
    fun longClickDeleteExcluirNotNow() {
        val response = "mockjson/myreservations/load_my_reservations_success.json".getJson()
        setResponse(response, 200)

        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.recyclerListError))
            .check(matches(not(isDisplayed())))
        onView(withRecyclerView(R.id.recyclerListView)
            .atPosition(0))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(withText("Eeee"))))
        onView(
            withRecyclerView(R.id.recyclerListView)
                .atPosition(0))
            .check(matches(isDisplayed()))
            .perform(ViewActions.longClick())

        onView(withText("A reserva do jogo Eeee sairá da lista de \"Minhas Reservas\", mas você poderá adicioná-lo novamente no futuro."))

        onView(withText("AGORA NÃO"))
            .check(matches(isDisplayed()))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(ViewActions.click())

        onView(withRecyclerView(R.id.recyclerListView)
            .atPosition(0))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant((withText("Eeee")))))
    }

    //TODO FIX BELLOW METHODS
    @Test
    fun testDeleteGameSuccess() {
        val response = "mockjson/myreservations/load_my_reservations_success.json".getJson()
        setResponse(response, 200)
        setResponse("mockjson/library/loan/lend_response_success.json".getJson(), 200)
        setResponse("mockjson/loadmygames/delete_success.json".getJson(), 200)
        setResponse("mockjson/loadmygames/load_my_games_success_after_delete.json".getJson(), 200)
        setResponse("mockjson/loadmygames/load_my_games_success_second.json".getJson(), 200)

        onView(withId(R.id.recyclerListView).childAtPosition(
            0
        ).childAtPosition(0)).check(matches(hasDescendant(withText("TEST"))))
        onView(withId(R.id.recyclerListView).childAtPosition(
            0
        ).childAtPosition(0)).perform(click())

        onView(withId(R.id.delete)).perform(click())
        onView(withText("EXCLUIR")).perform(click())
        onView(withId(R.id.recyclerListView).childAtPosition(
            0
        ).childAtPosition(0)).check(matches(hasDescendant(withText("FIFA 06: Road to FIFA World Cup"))))
    }

    @Test
    fun testDeleteGameError() {
        setResponse("mockjson/loadmygames/load_my_games_success.json".getJson(), 200)
        setResponse("mockjson/loadmygames/load_my_games_success_second.json".getJson(), 200)
        setResponse("mockjson/library/loan/lend_response_success.json".getJson(), 200)
        setResponse("mockjson/errors/error_400.json".getJson(), 400)

        onView(withText("TEST")).perform(click())
        onView(withId(R.id.delete)).perform(click())
        onView(withText("EXCLUIR")).perform(click())
        onView(withText("ERROR MESSAGE.")).check(matches(isDisplayed()))
    }

    private fun setResponse(json: String, code: Int) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(json)
        )
    }
}