package br.com.concrete.tentacle.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import br.com.concrete.tentacle.TentacleTestApp

/**
 * Custom runner to disable dependency injection.
 */
class TentacleTestRunner: AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TentacleTestApp::class.java.name, context)
    }


}