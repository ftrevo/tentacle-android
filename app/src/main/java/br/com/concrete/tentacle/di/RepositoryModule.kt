package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.data.repositories.UserRepositoryContract
import br.com.concrete.tentacle.data.repositories.LoginRepository
import org.koin.dsl.module.module

val repositoryModule = module{

    factory { UserRepository(get()) as UserRepositoryContract }
    factory { LoginRepository(get()) }

}