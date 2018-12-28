package br.com.concrete.tentacle.ui.register.custom_actions

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.app.AlertDialog
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import br.com.concrete.tentacle.custom.TentacleSearchSpinner
import org.hamcrest.Matcher

class SpinnerDialogSetSelectionViewAction(positon: Int): ViewAction {
    override fun getDescription(): String {
        return "Set position spinner"
    }

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(TentacleSearchSpinner::class.java)
    }

    override fun perform(uiController: UiController?, view: View?) {
        val tentacleSearchSpinner = view as TentacleSearchSpinner
        //tentacleSearchSpinner.
    }
}