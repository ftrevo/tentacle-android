package br.com.concrete.tentacle.base

import android.app.Application
import br.com.concrete.tentacle.BuildConfig

import br.com.concrete.tentacle.di.PROPERTY_BASE_URL
import br.com.concrete.tentacle.di.networkModule
import br.com.concrete.tentacle.di.repositoryModule
import br.com.concrete.tentacle.di.viewModelModule
import br.com.concrete.tentacle.di.sharedPreferencesModule
import org.koin.android.ext.android.startKoin

class AppTentacle : Application() {

    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Answers(), Crashlytics())

        startKoin(this,
            listOf(networkModule,
                viewModelModule,
                repositoryModule,
                sharedPreferencesModule),

            extraProperties = mapOf(PROPERTY_BASE_URL to BuildConfig.BASE_URL))
    }
}