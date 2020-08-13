package br.com.concrete.tentacle.features.registerGame.remoteGame

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.custom.ListCustom
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.utils.EMPTY_STRING
import br.com.concrete.tentacle.utils.HTTP_UPGRADE_REQUIRED
import br.com.concrete.tentacle.utils.TIME_PROGRESS_LOAD
import kotlinx.android.synthetic.main.fragment_remote_game.listCustom
import kotlinx.android.synthetic.main.list_custom.view.buttonAction
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import org.koin.android.viewmodel.ext.android.viewModel

class RemoteGameFragment : BaseFragment(), ListCustom.OnScrollListener {

    private val remoteViewModel: RemoteGameViewModel by viewModel()
    private var gameName: String = EMPTY_STRING
    private var gamesList = ArrayList<Game?>()
    private lateinit var recyclerViewAdapter: BaseAdapter<Game>
    private var loadMoreItems = true
    private var count = Int.MAX_VALUE

    override fun getToolbarTitle(): Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_remote_game, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initViews()
    }

    private fun initViews() {
        listCustom.setOnScrollListener(this)
        listCustom.recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        listCustom.recyclerListView.layoutManager = layoutManager

        arguments?.let { bundle ->
            gameName = RemoteGameFragmentArgs.fromBundle(bundle).gameName

            setupToolbar()
            addListener()
            addObservers()

            remoteViewModel.getRemoteGames(gameName)
        }
    }

    private fun addObservers() {
        remoteViewModel.remoteGamesViewState.observe(this, Observer { remoteGamesViewState ->
            when (remoteGamesViewState.status) {
                ViewStateModel.Status.LOADING -> {
                    enableProgress(true)
                }
                ViewStateModel.Status.SUCCESS -> {
                    enableProgress(false)
                    showList(remoteGamesViewState.model as ArrayList<Game?>)
                }
                ViewStateModel.Status.ERROR -> {
                    if (remoteGamesViewState.errors?.statusCode == HTTP_UPGRADE_REQUIRED) showError(remoteGamesViewState.errors, getString(R.string.was_some_mistake))
                    enableProgress(false)
                    loadMessageErrorLoading(remoteGamesViewState)
                }
            }
        })

        remoteViewModel.gameViewState.observe(this, Observer {
            it.getContentIfNotHandler()?.let { gameViewState ->
                when (gameViewState.status) {
                    ViewStateModel.Status.LOADING -> {
                        enableProgress(true)
                    }
                    ViewStateModel.Status.SUCCESS -> {
                        enableProgress(false)
                        gameViewState.model?.let {
                            navigateToRegisterPlatform(it)
                        }
                    }
                    ViewStateModel.Status.ERROR -> {
                        enableProgress(false)
                        showError(gameViewState.errors)
                    }
                }
            }
        })

        remoteViewModel.remoteGamesMoreViewState.observe(this, Observer { remoteGamesViewState ->
            when (remoteGamesViewState.status) {
                ViewStateModel.Status.LOADING -> {}
                ViewStateModel.Status.SUCCESS -> {
                    if (remoteGamesViewState.model.isNullOrEmpty()) {
                        finishLoadMore()
                    } else {
                        val gamesResponse = remoteGamesViewState.model
                        Handler().postDelayed({
                            recyclerViewAdapter.notifyItemInserted(gamesList.size - 1)
                            gamesList.removeAt(gamesList.size - 1)
                            recyclerViewAdapter.notifyItemRemoved(gamesList.size - 1)
                            gamesResponse?.let { gamesList.addAll(it) }
                            loadMoreItems = true
                            recyclerViewAdapter.setNewList(gamesList)
                        }, TIME_PROGRESS_LOAD)
                    }
                }
                ViewStateModel.Status.ERROR -> {
                    if (remoteGamesViewState.errors?.statusCode == HTTP_UPGRADE_REQUIRED) showError(remoteGamesViewState.errors, getString(R.string.was_some_mistake))
                    finishLoadMore()
                }
            }
        })
    }

    private fun finishLoadMore() {
        gamesList.removeAt(gamesList.size - 1)
        recyclerViewAdapter.notifyItemRemoved(gamesList.size - 1)
        loadMoreItems = false
        gamesList.add(Game.getEmptyGame())
        recyclerViewAdapter.notifyItemInserted(gamesList.size - 1)
        count = gamesList.size - 1
    }

    fun addListener() {
        listCustom.recyclerListError.buttonNameError.setOnClickListener {
            activity?.onBackPressed()
        }
        listCustom.buttonAction.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun showList(model: ArrayList<Game?>) {
        model.forEach { game ->
            game?.let { gamesList.add(it) }
        }
        if (model.isNotEmpty()) {
            fillRecyclerView(model)
        } else {
            listCustom.updateUi(model)
        }
    }

    private fun fillRecyclerView(modelItems: ArrayList<Game?>) {
        recyclerViewAdapter =
            BaseAdapter(modelItems,
                R.layout.item_game_search, {
                    RemoteGameViewHolder(it)
                }, { holder, element ->
                    RemoteGameViewHolder.callBack(holder = holder, game = element, listener = { gameSelected ->
                        gameSelected.remoteGameId?.let {
                            remoteViewModel.registerRemoteGame(it)
                        }
                    })
                })
        listCustom.recyclerListView.setItemViewCacheSize(modelItems.size)
        listCustom.recyclerListView.adapter = recyclerViewAdapter
        listCustom.updateUi(modelItems)
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = gameName
    }

    private fun enableProgress(isEnable: Boolean) {
        listCustom.setLoading(isEnable)
    }

    private fun loadMessageErrorLoading(remoteGameModel: ViewStateModel<ArrayList<Game>>) {
        remoteGameModel.errors?.let {
            listCustom.setErrorMessage(R.string.load_games_error_not_know)
            listCustom.setButtonTextError(R.string.remote_game_not_found_action_button)
            listCustom.setActionError {
                activity?.onBackPressed()
            }
        }
        listCustom.updateUi<Game>(null)
        listCustom.setLoading(false)
    }

    private fun navigateToRegisterPlatform(game: Game) {
        val directions = RemoteGameFragmentDirections.navigateToRegisterPlatformFromRemote(game._id)
        findNavController().navigate(directions)
    }

    override fun count() = count

    override fun sizeElements() = gamesList.size

    override fun loadMore() {
        if (loadMoreItems) {
            remoteViewModel.loadMore()
            gamesList.add(null)
            loadMoreItems = false

            recyclerViewAdapter.notifyItemInserted(gamesList.size - 1)
            gamesList.addAll(ArrayList<Game>())
            recyclerViewAdapter.setNewList(gamesList)
        }
    }

    override fun loadPage() = loadMoreItems
}