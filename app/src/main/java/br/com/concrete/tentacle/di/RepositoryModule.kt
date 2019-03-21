package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.data.repositories.FilterRepository
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.data.repositories.LoginRepository
import br.com.concrete.tentacle.data.repositories.RegisterMediaRepository
import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.data.repositories.LibraryRepository
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.data.repositories.TokenRepository
import org.koin.dsl.module.module

val repositoryModule = module {

    factory { UserRepository(get(API_WITHOUT_TOKEN), get()) }
    factory { LoginRepository(get(API_WITHOUT_TOKEN)) }
    factory { GameRepository(get(API_WITH_TOKEN)) }
    factory { RegisterMediaRepository(get(API_WITH_TOKEN)) }
    factory { LibraryRepository(get(API_WITH_TOKEN)) }
    factory { FilterRepository(get()) }
    factory { TokenRepository(get(API_WITH_TOKEN)) }

    single { SharedPrefRepository(get()) }
}