package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.data.eventPublisher.EventPublisher
import br.com.concrete.tentacle.data.eventPublisher.EventPublisherContract
import org.koin.dsl.module.module

val eventsModule = module {

    single { EventPublisher() as EventPublisherContract }
}