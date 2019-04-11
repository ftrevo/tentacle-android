package br.com.concrete.tentacle.features.loadmygames

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ActiveMedia
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.SingleEvent

class LoadMyGamesViewModel(private val gameRepository: GameRepository) : BaseViewModel() {

    private val viewStateGame: MutableLiveData<SingleEvent<ViewStateModel<MediaResponse>>> = MutableLiveData()
    private val viewStateGameDelete: MutableLiveData<SingleEvent<ViewStateModel<Media>>> = MutableLiveData()
    private val viewStateGamePage: MutableLiveData<SingleEvent<ViewStateModel<MediaResponse>>> = MutableLiveData()
    private val viewStateGameActive: MutableLiveData<SingleEvent<ViewStateModel<Media>>> = MutableLiveData()
    fun getMyGames(): LiveData<SingleEvent<ViewStateModel<MediaResponse>>> = viewStateGame
    fun getMyGamesPage(): LiveData<SingleEvent<ViewStateModel<MediaResponse>>> = viewStateGamePage
    fun deleteMedia(): LiveData<SingleEvent<ViewStateModel<Media>>> = viewStateGameDelete
    fun activeMediaState(): LiveData<SingleEvent<ViewStateModel<Media>>> = viewStateGameActive

    private var page: Int = 1

    var queryParameters: QueryParameters? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun preLoadMyGames() {
        loadMyGames()
    }

    fun loadMyGames() {
        val queries = queryParameters ?: QueryParameters()

        viewStateGame.postValue(SingleEvent(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(
            gameRepository.loadMyGames(queries)
                .subscribe({ baseModel ->
                    viewStateGame.postValue(
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                }, {
                    viewStateGame.postValue(
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.ERROR,
                                errors = notKnownError(it)
                            )
                        )
                    )
                })
        )
    }

    fun loadGamePage() {
        val queries = queryParameters ?: QueryParameters()
        queries.page = page

        disposables.add(
            gameRepository.loadMyGames(queries)
                .subscribe({ baseModel ->
                    viewStateGamePage.postValue(
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                    page += 1
                }, {
                    viewStateGamePage.postValue(
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.ERROR,
                                errors = notKnownError(it)
                            )
                        )
                    )
                })
        )
    }

    fun deleteGame(id: String) {
        disposables.add(
            gameRepository.deleteMedia(id)
                .subscribe({ baseModel ->
                    viewStateGameDelete.postValue(
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                }, {
                    viewStateGameDelete.postValue(
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.ERROR,
                                errors = notKnownError(it)
                            )
                        )
                    )
                })
        )
    }

    fun activeMedia(media: Media, active: Boolean) {
        val activeMedia = ActiveMedia(active)

        disposables.add(
            gameRepository.activeMedia(media._id, activeMedia)
                .subscribe({ baseModel ->
                    viewStateGameActive.postValue(
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                }, {
                    viewStateGameActive.postValue(
                        SingleEvent(
                            ViewStateModel(
                                status = ViewStateModel.Status.ERROR,
                                errors = notKnownError(it)
                            )
                        )
                    )
                })
        )
    }

    fun resetPage() {
        this.page = 1
    }
}
