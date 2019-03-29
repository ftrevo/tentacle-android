package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.base.Publisher
import org.koin.dsl.module.module

val eventsModule = module {

    single { Publisher() }

}