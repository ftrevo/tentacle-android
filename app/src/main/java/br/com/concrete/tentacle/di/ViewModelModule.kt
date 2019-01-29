package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.features.library.LibraryViewModel
import br.com.concrete.tentacle.features.loadmygames.LoadMyGamesViewModel
import br.com.concrete.tentacle.features.login.LoginViewModel
import br.com.concrete.tentacle.features.register.RegisterUserViewModel
import br.com.concrete.tentacle.features.registerGame.registerMedia.RegisterMediaViewModel
import br.com.concrete.tentacle.features.registerGame.searchGame.SearchGameViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {

    viewModel { RegisterUserViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SearchGameViewModel(get()) }
    viewModel { LoadMyGamesViewModel(get()) }
    viewModel { RegisterMediaViewModel(get()) }
    viewModel { LibraryViewModel(get()) }
}