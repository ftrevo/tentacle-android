package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.data.repositories.UserRepository
import org.koin.dsl.module.module

val repositoryModule = module{

    factory { UserRepository(get()) }
    factory { LoginRepository(get()) }

}