package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.features.login.LoginViewModel
import org.koin.dsl.module.module

val repositoryModule = module{

    factory { LoginRepository(get()) }

}