package br.com.concrete.tentacle.di

import androidx.lifecycle.ViewModelProviders
import br.com.concrete.tentacle.features.login.LoginViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    //viewModel { RepoViewModel(get()) } //TODO Sample to be replaced
    viewModel { LoginViewModel(get(), get()) }
}