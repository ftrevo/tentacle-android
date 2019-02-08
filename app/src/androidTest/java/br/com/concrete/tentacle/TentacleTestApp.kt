package br.com.concrete.tentacle

import android.app.Application
import android.os.AsyncTask
import br.com.concrete.tentacle.di.PROPERTY_BASE_URL
import br.com.concrete.tentacle.di.androidModule
import br.com.concrete.tentacle.di.networkModule
import br.com.concrete.tentacle.di.repositoryModule
import br.com.concrete.tentacle.di.viewModelModule
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.startKoin

private const val LOCALHOST_URL = "http://localhost:8080/"

class TentacleTestApp : Application() {

    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setInitComputationSchedulerHandler { Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR) }
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR) }

        startKoin(this,
            listOf(androidModule,
                networkModule,
                viewModelModule,
                repositoryModule
            ),

            extraProperties = mapOf(PROPERTY_BASE_URL to LOCALHOST_URL))
    }
}