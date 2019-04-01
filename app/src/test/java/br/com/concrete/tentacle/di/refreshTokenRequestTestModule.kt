package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.data.refreshToken.RefreshTokenRequestMock
import org.koin.dsl.module.module

val refreshTokenRequestTestModule = module {

    single { RefreshTokenRequestMock(get()) }

}