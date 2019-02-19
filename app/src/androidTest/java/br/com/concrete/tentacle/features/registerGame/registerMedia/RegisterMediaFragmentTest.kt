package br.com.concrete.tentacle.features.registerGame.registerMedia

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragmentTest
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.fromJson
import br.com.concrete.tentacle.extensions.getJson
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterMediaFragmentTest : BaseFragmentTest() {

    private val responseJson =
        "mockjson/loadmygames/game_register_media_success.json".getJson()

    private val expectedGame = Gson().fromJson<Game>(responseJson)

    override fun setupFragment() {
        testFragment = RegisterMediaFragment().apply {
            val args = Bundle()
            args.putParcelable("game_argument", expectedGame)
            arguments = args
        }
    }

    @Test
    fun showRegisteredGameName() {
        onView(withId(R.id.mediaRegisterNameTextView))
            .check(matches(withText(expectedGame.name)))
    }

    @Test
    fun showSnackBarWhenNoPlatformIsSelected() {
        onView(withId(R.id.mediaRegisterButton))
            .perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.register_media_no_selection_error)))
    }

    @Test
    fun registerMediaPlatformError() {
        onView(withId(R.id.mediaPS4RadioButton))
            .perform(click())

        mockWebServer.enqueue(MockResponse()
            .setBody(Gson().toJson(Throwable()))
            .setResponseCode(400))

        onView(withId(R.id.mediaRegisterButton))
            .perform(click())

        pressBack()
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.register_media_generic_error_test)))
    }

    @Test
    fun registerMediaPlatformSuccess() {

        onView(withId(R.id.mediaPS4RadioButton))
            .perform(click())

        val response =
            "mockjson/loadmygames/register_media_success.json".getJson()

        mockWebServer.enqueue(MockResponse()
            .setBody(response)
            .setResponseCode(201))

        onView(withId(R.id.mediaRegisterButton))
            .perform(click())

        assertTrue(activityRule.activity.isFinishing)
    }
}