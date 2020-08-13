package br.com.concrete.tentacle.features.library.loan

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.data.repositories.LibraryRepository
import br.com.concrete.tentacle.utils.LogWrapper

class LoanViewModel(
    private val libraryRepository: LibraryRepository,
    private val gameRepository: GameRepository
) : BaseViewModel() {

    private val viewStateLibrary: MutableLiveData<ViewStateModel<Library>> = MutableLiveData()
    private val viewStateLoan: MutableLiveData<ViewStateModel<LoanResponse>> = MutableLiveData()
    private val viewStateModelGame = MutableLiveData<ViewStateModel<Game>>()

    fun getLibrary() = viewStateLibrary
    fun getLoan() = viewStateLoan
    fun getGame() = viewStateModelGame

    fun loadLibrary(id: String) {
        viewStateLibrary.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            libraryRepository.getLibrary(id)
                .subscribe({ baseModel ->
                    viewStateLibrary.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = baseModel.data
                        )
                    )
                }, {
                    viewStateLibrary.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                })
        )
    }

    fun performLoad(mediaId: String) {
        viewStateLoan.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            libraryRepository.performLoan(mediaId)
                .subscribe({ baseModel ->
                    viewStateLoan.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = baseModel.data
                        )
                    )
                }, {
                    viewStateLoan.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                })
        )
    }

    fun getDetailsGame(idGame: String) {
        viewStateModelGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(
            gameRepository.getDetailsGame(idGame)
                .subscribe({
                    viewStateModelGame.postValue(
                        ViewStateModel(
                            ViewStateModel.Status.SUCCESS, it.data
                        )
                    )
                },
                    {
                        viewStateModelGame.postValue(
                            ViewStateModel(
                                status = ViewStateModel.Status.ERROR,
                                errors = notKnownError(it)
                            )
                        )
                        LogWrapper.print(it)
                    })
        )
    }
}