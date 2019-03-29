package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract
import br.com.concrete.tentacle.repositories.SharedPrefRepositoryMock
import org.koin.dsl.module.module

val mockAndroidModule = module {

    single { SharedPrefRepositoryMock() as SharedPrefRepositoryContract }
}