package br.com.concrete.tentacle.base

import PROPERTY_BASE_URL
import android.app.Application
import br.com.concrete.tentacle.BuildConfig
import br.com.concrete.tentacle.di.repositoryModule
import br.com.concrete.tentacle.di.sharedPreferencesModule
import br.com.concrete.tentacle.di.viewModelModule
import networkModule
import org.koin.android.ext.android.startKoin

class AppTentacle : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this,
            listOf(networkModule,
                viewModelModule,
                repositoryModule,
                sharedPreferencesModule),

            extraProperties = mapOf(PROPERTY_BASE_URL to BuildConfig.BASE_URL))
    }
}