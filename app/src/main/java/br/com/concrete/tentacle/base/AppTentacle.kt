package br.com.concrete.tentacle.base

import android.app.Application
import br.com.concrete.tentacle.BuildConfig
import br.com.concrete.tentacle.di.*
import org.koin.android.ext.android.startKoin

class AppTentacle : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this,
            listOf(networkModule,
                viewModelModule,
                repositoryModule,
                sharedPresentException),
            extraProperties = mapOf(PROPERTY_BASE_URL to BuildConfig.BASE_URL))
    }
}