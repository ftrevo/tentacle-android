package br.com.concrete.tentacle.di

import API_WITHOUT_TOKEN
import API_WITH_TOKEN
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.data.repositories.RegisterMediaRepository
import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.data.repositories.LibraryRepository
import org.koin.dsl.module.module

val repositoryModule = module {

    factory { UserRepository(get(API_WITHOUT_TOKEN)) }
    factory { LoginRepository(get(API_WITHOUT_TOKEN)) }
    factory { GameRepository(get(API_WITH_TOKEN)) }
    factory { RegisterMediaRepository(get(API_WITH_TOKEN)) }
    factory { LibraryRepository(get(API_WITH_TOKEN)) }
}