package br.com.concrete.tentacle.base

import android.app.Activity
import android.app.Instrumentation
import androidx.navigation.NavHost
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.childAtPosition
import br.com.concrete.tentacle.testing.SingleFragmentTestActivityNoActionBarNoBottomBar
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import java.io.File

@Ignore
abstract class BaseFragmentNoActionBarNoBottomBarTest : BaseInstrumentedTest() {

    companion object {
        var intentsInitialized = false

        fun init() {
            if (!intentsInitialized) {
                Intents.init()
                intentsInitialized = true
            }
        }
    }

    @get:Rule
    val activityRule = ActivityTestRule(SingleFragmentTestActivityNoActionBarNoBottomBar::class.java)

    abstract fun setupFragment()

    @Before
    open fun before() {
        clearApplicationData()
        setupFragment()
        init()

        testFragment.let {
            activityRule.activity.setFragment(it)
        }
    }

    fun setField(textField: String, id: Int) {
        onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edt),
                ViewMatchers.withId(id)
                    .childAtPosition(0)
                    .childAtPosition(1)
            )
        ).perform(
            ViewActions.scrollTo(),
            ViewActions.replaceText(textField)
        )
    }

    private fun clearApplicationData() {
        val cacheDirectory = activityRule.activity.cacheDir
        val applicationDirectory = File(cacheDirectory.parent)
        if (applicationDirectory.exists()) {
            val fileNames = applicationDirectory.list()
            for (fileName in fileNames) {
                if (fileName != "lib") {
                    deleteFile(File(applicationDirectory, fileName))
                }
            }
        }
    }

    private fun deleteFile(file: File?): Boolean {
        var deletedAll = true
        if (file != null) {
            if (file.isDirectory) {
                val children = file.list()
                for (i in children.indices) {
                    deletedAll = deleteFile(File(file, children[i])) && deletedAll
                }
            } else {
                deletedAll = file!!.delete()
            }
        }

        return deletedAll
    }

    fun checkIfGoesToHome() {
        val matcher = CoreMatchers.allOf(IntentMatchers.hasComponent(NavHost::class.java.name))
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        Intents.intending(matcher).respondWith(result)
    }
}