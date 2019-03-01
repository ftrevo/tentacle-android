package br.com.concrete.tentacle.myreservations

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.extensions.getJson
import br.com.concrete.tentacle.extensions.waitUntil
import br.com.concrete.tentacle.features.myreservations.MyReservationFragment
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

}