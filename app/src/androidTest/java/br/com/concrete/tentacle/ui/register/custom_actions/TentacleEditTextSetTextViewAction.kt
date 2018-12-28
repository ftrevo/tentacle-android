package br.com.concrete.tentacle.ui.register.custom_actions

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import br.com.concrete.tentacle.custom.TentacleEditText
import kotlinx.android.synthetic.main.tentacle_edit_text_layout.view.*
import org.hamcrest.Matcher

class TentacleEditTextSetTextViewAction(private val text: String): ViewAction {

    override fun getDescription(): String {
        return "Set text in editText"
    }

    override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(TentacleEditText::class.java)
    }

    override fun perform(uiController: UiController?, view: View?) {
        val tentacleEditText = view as TentacleEditText
        tentacleEditText.edt.setText(text)
    }
}