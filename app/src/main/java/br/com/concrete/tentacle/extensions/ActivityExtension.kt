package br.com.concrete.tentacle.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.features.login.LoginActivity

enum class ActivityAnimation {
    TRANSLATE_LEFT, TRANSLATE_RIGHT, TRANSLATE_UP, TRANSLATE_DOWN, TRANSLATE_FADE
}

inline fun <reified T : Activity> Activity.launchActivity(
    animation: ActivityAnimation = ActivityAnimation.TRANSLATE_LEFT
) {
    val intent = Intent(this, T::class.java)
    startActivity(intent)

    val animations = getAnimation(animation)
    overridePendingTransition(animations[0], animations[1])
}

inline fun <reified T : Activity> Activity.launchActivity(
    extras: Bundle? = null,
    animation: ActivityAnimation = ActivityAnimation.TRANSLATE_LEFT
) {
    val intent = Intent(this, T::class.java)

    extras?.let {
        intent.putExtras(it)
    }

    startActivity(intent)

    val animations = getAnimation(animation)
    overridePendingTransition(animations[0], animations[1])
}

inline fun <reified T : Activity> Activity.launchActivity(
    extras: Bundle? = null,
    intentFlags: Int,
    animation: ActivityAnimation = ActivityAnimation.TRANSLATE_LEFT
) {
    val intent = Intent(this, T::class.java)
    intent.flags = intentFlags

    extras?.let {
        intent.putExtras(it)
    }

    startActivity(intent)

    val animations = getAnimation(animation)
    overridePendingTransition(animations[0], animations[1])
}

inline fun <reified T : Activity> Activity.launchActivity(
    extras: Bundle? = null,
    activityOptions: ActivityOptionsCompat,
    animation: ActivityAnimation = ActivityAnimation.TRANSLATE_LEFT
) {

    val intent = Intent(this, T::class.java)

    extras?.let {
        intent.putExtras(it)
    }

    ActivityCompat.startActivity(this, intent, activityOptions.toBundle())

    val animations = getAnimation(animation)
    overridePendingTransition(animations[0], animations[1])
}

inline fun <reified T : Activity> Activity.launchActivityForResult(
    requestCode: Int,
    animation: ActivityAnimation = ActivityAnimation.TRANSLATE_LEFT
) {

    val intent = Intent(this, T::class.java)

    startActivityForResult(intent, requestCode)

    val animations = getAnimation(animation)
    overridePendingTransition(animations[0], animations[1])
}

inline fun <reified T : Activity> Activity.launchActivityForResult(
    requestCode: Int,
    extras: Bundle? = null,
    animation: ActivityAnimation = ActivityAnimation.TRANSLATE_LEFT
) {

    val intent = Intent(this, T::class.java)

    extras?.let {
        intent.putExtras(it)
    }

    startActivityForResult(intent, requestCode)

    val animations = getAnimation(animation)
    overridePendingTransition(animations[0], animations[1])
}

inline fun <reified T : Activity> Activity.launchActivityForResult(
    requestCode: Int,
    extras: Bundle? = null,
    activityOptions: ActivityOptionsCompat,
    animation: ActivityAnimation = ActivityAnimation.TRANSLATE_LEFT
) {

    val intent = Intent(this, T::class.java)

    extras?.let {
        intent.putExtras(it)
    }

    ActivityCompat.startActivityForResult(this, intent, requestCode,
            activityOptions.toBundle())

    val animations = getAnimation(animation)
    overridePendingTransition(animations[0], animations[1])
}

fun Activity.finishActivity(animation: ActivityAnimation = ActivityAnimation.TRANSLATE_RIGHT) {
    finish()
    val animations = getAnimation(animation)
    overridePendingTransition(animations[0], animations[1])
}

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus
    view?.let {
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Activity.showKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus
    view?.let {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

fun Activity.logout() {
    this.launchActivity<LoginActivity>(
        animation = ActivityAnimation.TRANSLATE_UP,
        intentFlags = (Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK))
    this.finish()
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun getAnimation(animation: ActivityAnimation?): IntArray {
    val exitAnim: Int
    val enterAnim: Int

    when (animation) {

        ActivityAnimation.TRANSLATE_UP -> {
            enterAnim = R.anim.translate_slide_up
            exitAnim = R.anim.translate_no_change
        }

        ActivityAnimation.TRANSLATE_DOWN -> {
            enterAnim = R.anim.translate_no_change
            exitAnim = R.anim.translate_slide_down
        }

        ActivityAnimation.TRANSLATE_RIGHT -> {
            enterAnim = R.anim.translate_right_enter
            exitAnim = R.anim.translate_right_exit
        }

        ActivityAnimation.TRANSLATE_FADE -> {
            enterAnim = R.anim.translate_fade_in
            exitAnim = R.anim.translate_fade_out
        }

        else -> {
            enterAnim = R.anim.translate_left_enter
            exitAnim = R.anim.translate_left_exit
        }
    }

    return intArrayOf(enterAnim, exitAnim)
}