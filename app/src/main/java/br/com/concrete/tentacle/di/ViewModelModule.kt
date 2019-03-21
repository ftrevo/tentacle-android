package br.com.concrete.tentacle.di

import br.com.concrete.tentacle.features.forgotPassword.passwordRecovery.PasswordRecoveryViewModel
import br.com.concrete.tentacle.features.forgotPassword.sendEmail.ForgotPassSendEmailViewModel
import br.com.concrete.tentacle.features.home.HomeViewModel
import br.com.concrete.tentacle.features.lendgame.LendGameViewModel
import br.com.concrete.tentacle.features.library.LibraryViewModel
import br.com.concrete.tentacle.features.library.loan.LoanViewModel
import br.com.concrete.tentacle.features.library.filter.FilterViewModel
import br.com.concrete.tentacle.features.loadmygames.LoadMyGamesViewModel
import br.com.concrete.tentacle.features.login.LoginViewModel
import br.com.concrete.tentacle.features.myreservations.MyReservationViewModel
import br.com.concrete.tentacle.features.myreservations.detail.MyReservationDetailViewModel
import br.com.concrete.tentacle.features.register.RegisterUserViewModel
import br.com.concrete.tentacle.features.registerGame.registerMedia.RegisterMediaViewModel
import br.com.concrete.tentacle.features.registerGame.remoteGame.RemoteGameViewModel
import br.com.concrete.tentacle.features.registerGame.searchGame.SearchGameViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {

    viewModel { RegisterUserViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { SearchGameViewModel(get()) }
    viewModel { LoadMyGamesViewModel(get()) }
    viewModel { RegisterMediaViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LibraryViewModel(get()) }
    viewModel { FilterViewModel(get()) }
    viewModel { LoanViewModel(get(), get()) }
    viewModel { LendGameViewModel(get()) }
    viewModel { RemoteGameViewModel(get()) }
    viewModel { MyReservationViewModel(get()) }
    viewModel { MyReservationDetailViewModel(get()) }
    viewModel { ForgotPassSendEmailViewModel(get()) }
    viewModel { PasswordRecoveryViewModel(get(), get()) }
}