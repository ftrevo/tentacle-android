package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import org.koin.dsl.module.module

val sharedPresentException = module {

    single {
        val sharedPrefRepository = SharedPrefRepository(get())
        sharedPrefRepository
    }

}