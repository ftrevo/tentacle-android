package br.com.concrete.tentacle.features.searchGame

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.testing.OpenForTesting
import io.reactivex.disposables.CompositeDisposable

@OpenForTesting
class SearchGameViewModel(
    private val gameRepository: GameRepository,
    private val sharedPrefRepository: SharedPrefRepository
) : BaseViewModel(),
    LifecycleObserver {

    private val viewSearchGame = MutableLiveData<ViewStateModel<ArrayList<Game>>>()
    private val viewGame = MutableLiveData<ViewStateModel<Game>>()
    private val disposables = CompositeDisposable()

    fun searchGame(title: String) {
        viewSearchGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))

        disposables.add(
            gameRepository.getSearchGames(
                title
            ).subscribe(
                { base ->
                    viewSearchGame.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.SUCCESS,
                            model = base.data.list as ArrayList<Game>
                        )
                    )
                }, {
                    viewSearchGame.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = notKnownError(it)
                        )
                    )
                })
        )
    }

    fun registerNewGame(title: String) {
        viewGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))

        disposables.add(gameRepository.registerNewGame(
            GameRequest(title)
        ).subscribe(
            { base ->
                viewGame.postValue(
                    ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = base.data
                    )
                )
            }, {
                viewGame.postValue(
                    ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it)
                    )
                )
            })
        )
    }

    fun getSearchGame() = viewSearchGame
    fun getRegisteredGame() = viewGame

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}