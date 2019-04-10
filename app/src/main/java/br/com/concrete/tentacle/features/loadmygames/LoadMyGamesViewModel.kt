package br.com.concrete.tentacle.features.loadmygames

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.enums.Platform
import br.com.concrete.tentacle.data.models.ActiveMedia
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaRequest
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.Event
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_3DS
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_3DS_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_SWITCH
import br.com.concrete.tentacle.utils.PLATFORM_NINTENDO_SWITCH_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_PS3_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_PS4_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_360
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_360_ABBREV
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_ONE
import br.com.concrete.tentacle.utils.PLATFORM_XBOX_ONE_ABBREV

class LoadMyGamesViewModel(private val gameRepository: GameRepository) : BaseViewModel() {

    private val viewStateGame: MutableLiveData<Event<ViewStateModel<MediaResponse>>> = MutableLiveData()
    private val viewStateGameDelete: MutableLiveData<Event<ViewStateModel<Media>>> = MutableLiveData()
    private val viewStateGamePage: MutableLiveData<Event<ViewStateModel<MediaResponse>>> = MutableLiveData()
    private val viewStateGameActive: MutableLiveData<Event<ViewStateModel<Media>>> = MutableLiveData()

    fun getMyGames(): LiveData<Event<ViewStateModel<MediaResponse>>> = viewStateGame
    fun getMyGamesPage(): LiveData<Event<ViewStateModel<MediaResponse>>> = viewStateGamePage
    fun deleteMedia(): LiveData<Event<ViewStateModel<Media>>> = viewStateGameDelete
    fun activeMediaState(): LiveData<Event<ViewStateModel<Media>>> = viewStateGameActive

    private var page: Int = 1

    var queryParameters: QueryParameters? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun preLoadMyGames() {
        loadMyGames()
    }

    fun loadMyGames() {
        val queries = queryParameters ?: QueryParameters()

        viewStateGame.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(
            gameRepository.loadMyGames(queries)
                .subscribe({ baseModel ->
                    viewStateGame.postValue(
                        Event(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                }, {
                    viewStateGame.postValue(
                        Event(
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
                        Event(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                    page += 1
                }, {
                    viewStateGamePage.postValue(
                        Event(
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
                        Event(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                }, {
                    viewStateGameDelete.postValue(
                        Event(
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
                        Event(
                            ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = baseModel.data
                            )
                        )
                    )
                }, {
                    viewStateGameActive.postValue(
                        Event(
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
