package br.com.concrete.tentacle.base


import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        const val INVALID_ICON = -1
        const val INVALID_TITLE = -1
    }

    fun setupToolbar() {
        setupToolbar(INVALID_TITLE, INVALID_ICON, true)
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
        return when(item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}