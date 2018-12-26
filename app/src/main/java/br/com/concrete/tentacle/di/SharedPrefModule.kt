package br.com.concrete.tentacle.di

import android.content.Context
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

private const val TENTACLE_PREFERENCES_NAME = "TENTACLE_PREFERENCES_NAME"

val sharedPreferencesModule = module {

    single {
        androidContext().getSharedPreferences(TENTACLE_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    single {
        val sharedPrefRepository = SharedPrefRepository(get())
        sharedPrefRepository
    }

}