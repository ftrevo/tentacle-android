package br.com.concrete.tentacle.base

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.logout
import br.com.concrete.tentacle.features.HostActivity
import br.com.concrete.tentacle.utils.DialogUtils
import io.reactivex.functions.Consumer
import org.koin.android.viewmodel.ext.android.viewModel
import java.net.HttpURLConnection
import br.com.concrete.tentacle.utils.HTTP_UPGRADE_REQUIRED

abstract class BaseActivity : AppCompatActivity() {

    private val baseViewModel: BaseViewModel by viewModel()

    companion object {
        const val INVALID_ICON = -1
        const val INVALID_TITLE = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        baseViewModel.subscribe(::getEventObserver)
    }

    private fun getEventObserver() = Consumer<Any> {
        when (it) {
            is Int -> {
                if (it == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    DialogUtils.showDialog(
                        context = this,
                        title = getString(R.string.something_happened),
                        message = getString(R.string.session_expired_text),
                        positiveText = getString(R.string.ok),
                        contentView = R.layout.custom_dialog_error,
                        positiveListener = DialogInterface.OnClickListener { _, _ ->
                            logout()
                        }
                    )
                }
            }
        }
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

    fun setSupportActionBarWithIcon(toolbar: Toolbar?, @StringRes title: Int, @DrawableRes icon: Int) {
        super.setSupportActionBar(toolbar)
        setupToolbar(title, icon)
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

            if (icon != -1 && displayHome) {
                actionBar.setHomeAsUpIndicator(icon)
            }
            actionBar.setDisplayShowHomeEnabled(displayHome)
            actionBar.setDisplayHomeAsUpEnabled(displayHome)
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
        var positiveButton = getString(R.string.ok)
        var negativeButton: String? = null

        errors?.let { errorResponse ->
            errorResponse.messageInt.map { error ->
                errorResponse.message.add(getString(error))
            }

            val ers = errorResponse.toString()
            var positive = DialogInterface.OnClickListener { _, _ -> }

            if (errors.statusCode == HTTP_UPGRADE_REQUIRED) {
                positiveButton = getString(R.string.update)
                negativeButton = getString(R.string.not_delete)
                positive = DialogInterface.OnClickListener { _, _ ->
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                errors.updateUrl
                            )
                        )
                    )
                }
            }

            DialogUtils.showDialog(
                context = this,
                title = if (title == "Erro") getString(R.string.something_happened) else title,
                message = ers,
                positiveText = positiveButton,
                positiveListener = positive,
                negativeText = negativeButton,
                contentView = R.layout.custom_dialog_error
            )
        }
    }

    open fun getFinishActivityTransition(): ActivityAnimation {
        return ActivityAnimation.TRANSLATE_RIGHT
    }
}