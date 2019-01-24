package br.com.concrete.tentacle.util

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import androidx.test.runner.AndroidJUnitRunner
import br.com.concrete.tentacle.TestApp
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

class TentacleTestRunner: AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }

    override fun onStart() {
        super.onStart()
        RxJavaPlugins.setInitComputationSchedulerHandler { Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR) }
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR) }
    }
}