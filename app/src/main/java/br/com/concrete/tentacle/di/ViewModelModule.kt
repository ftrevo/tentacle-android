package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.data.repositories.UserRepository
import br.com.concrete.tentacle.data.repositories.UserRepositoryContract
import br.com.concrete.tentacle.features.user.UserViewModel
import br.com.concrete.tentacle.features.user.UserViewModelContract
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {

    viewModel { UserViewModel(get()) }
}