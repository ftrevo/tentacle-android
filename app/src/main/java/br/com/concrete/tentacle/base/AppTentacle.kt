package br.com.concrete.tentacle.base

import android.app.Application
import br.com.concrete.tentacle.di.networkModule
import br.com.concrete.tentacle.di.viewModelModule
import org.koin.android.ext.android.startKoin

class AppTentacle : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(networkModule, viewModelModule))
    }
}