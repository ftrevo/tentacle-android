package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.features.loadmygames.LoadMyGamesViewModel
import br.com.concrete.tentacle.features.register.RegisterUserViewModel
import br.com.concrete.tentacle.features.login.LoginViewModel
import br.com.concrete.tentacle.features.registerMedia.RegisterMediaViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {

    viewModel { RegisterUserViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { LoadMyGamesViewModel(get()) }
    viewModel { RegisterMediaViewModel(get()) }
}