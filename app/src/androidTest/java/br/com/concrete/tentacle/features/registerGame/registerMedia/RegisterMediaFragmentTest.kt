package br.com.concrete.tentacle.features.registerGame.registerMedia

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.fromJson
import br.com.concrete.tentacle.extensions.getJson
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Test

class RegisterMediaFragmentTest : BaseFragmentTest() {

    private val responseJson =
        "mockjson/registerMedia/detail_game_success.json".getJson()

    private val mediaSaveSuccess =
        "mockjson/registerMedia/register_media_success.json".getJson()

    private val mediaAlreadyRegistered =
        "mockjson/errors/error_400.json".getJson()

    private val expectedGame = Gson().fromJson<BaseModel<Game>>(responseJson)

    override fun setupFragment() {
        testFragment = RegisterMediaFragment().apply {
            val args = Bundle()
            args.putSerializable("game_argument", expectedGame.data._id)
            arguments = args
        }
    }

    @Test
    fun showDetailGame() {

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mediaSaveSuccess)
        )

        checkDetails()

        onView(withId(R.id.mediaRegisterButton))
            .perform(scrollTo())
        onView(withId(R.id.mediaRegisterButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.mediaRegisterButton))
            .check(matches(isEnabled()))
    }

    @Test
    fun registerMedia() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mediaSaveSuccess)
        )

        checkDetails()

        onView(withId(R.id.mediaRegisterButton))
            .perform(scrollTo())
        onView(withId(R.id.mediaRegisterButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.mediaRegisterButton))
            .check(matches(isEnabled()))
        onView(withId(R.id.mediaRegisterButton))
            .perform(click())
        onView(withText("Você confirma que FIFA 06: Road to FIFA World Cup existe para a plataforma PS3?"))
            .check(matches(isDisplayed()))
        onView(withText("Confirmar"))
            .check(matches(isDisplayed()))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        Assert.assertTrue(activityRule.activity.isFinishing)
    }

    @Test
    fun registerMediaErroMediaAlreadyRegistered() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(400)
            .setBody(mediaAlreadyRegistered))

        checkDetails()

        onView(withId(R.id.mediaRegisterButton))
            .perform(scrollTo())

        onView(withId(R.id.mediaRegisterButton))
            .check(matches(isDisplayed()))

        onView(withId(R.id.mediaRegisterButton))
            .check(matches(isEnabled()))

        onView(withId(R.id.mediaRegisterButton))
            .perform(click())
        onView(withText("Você confirma que FIFA 06: Road to FIFA World Cup existe para a plataforma PS3?"))
            .check(matches(isDisplayed()))
        onView(withText("Confirmar"))
            .check(matches(isDisplayed()))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText("Ops! Parece que já cadastrou esse jogo."))
            .check(matches(isDisplayed()))
        onView(withText("ERROR MESSAGE."))
            .check(matches(isDisplayed()))
        onView(withText("Ok"))
            .check(matches(isDisplayed()))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun checkDetails() {
        onView(withId(R.id.tvGameName))
            .check(matches(withText("FIFA 06: Road to FIFA World Cup")))
        onView(withId(R.id.tvGameSummary))
            .check(matches(withText("You can experience the excitement of international soccer competition in FIFA 06. Extensive coaching options give you the opportunity to take control of all of your players on the pitch and determine their formation and strategy. FIFA 06 offers game modes ranging from a tournament to individual skill games. You can also play with real players from famous international teams from around the world. Multiplayer modes let you challenge other gamers.")))
        onView(withId(R.id.tvGameName))
            .check(matches(withText("FIFA 06: Road to FIFA World Cup")))
        onView(withId(R.id.tvGameReleaseYear))
            .check(matches(withText("2005")))
        onView(withText("Sport")).check(matches(isDisplayed()))
        onView(withText("Single player")).check(matches(isDisplayed()))
        onView(withText("Multiplayer")).check(matches(isDisplayed()))
        onView(withText("Co-operative")).check(matches(isDisplayed()))

        onView(withId(R.id.chipPs3))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.chipPs4))
            .check(matches(isDisplayed()))
        onView(withId(R.id.chip360))
            .check(matches(isDisplayed()))
        onView(withId(R.id.chipOne))
            .check(matches(isDisplayed()))
        onView(withId(R.id.chipOne))
            .check(matches(isDisplayed()))
        onView(withId(R.id.chip3ds))
            .check(matches(isDisplayed()))
    }
}