package br.com.concrete.tentacle.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.features.HostActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        const val INVALID_ICON = -1
        const val INVALID_TITLE = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun setupToolbar() {
        setupToolbar(INVALID_TITLE, INVALID_ICON, true)
    }

    fun setupToolbar(@StringRes title: Int, @DrawableRes icon: Int) {
        setupToolbar(title, icon, true)
    }

    fun setupToolbar(@DrawableRes icon: Int) {
        setupToolbar(INVALID_TITLE, icon, true)
    }

    fun setupToolbar(displayHome: Boolean) {
        setupToolbar(INVALID_TITLE, INVALID_ICON, displayHome)
    }

    fun setupToolbar(
        title: Int,
        icon: Int,
        displayHome: Boolean = true
    ) {

        supportActionBar?.let { actionBar ->
            if (title != INVALID_TITLE) {
                setToolbarTitle(title)
            }

            actionBar.setDisplayShowHomeEnabled(displayHome)
            actionBar.setDisplayHomeAsUpEnabled(displayHome)
            if (icon != -1 && displayHome) {
                actionBar.setHomeAsUpIndicator(icon)
            }
        }
    }

    fun setToolbarTitle(@StringRes title: Int) {
        supportActionBar?.let {
            it.title = getString(title)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (this !is HostActivity) {
                    onBackPressed()
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected open fun showError(errors: ErrorResponse?, title: String = "Erro") {
        errors?.let { errorResponse ->
            errorResponse.messageInt.map { error ->
                errorResponse.message.add(getString(error))
            }

            val ers = errorResponse.toString()

            val builder = AlertDialog.Builder(this)
            builder.setTitle(title)
            builder.setMessage(ers)
            builder.apply {
                setPositiveButton(
                    R.string.ok
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            }

            builder.create().show()
        }
    }

    open fun getFinishActivityTransition(): ActivityAnimation {
        return ActivityAnimation.TRANSLATE_RIGHT
    }
}