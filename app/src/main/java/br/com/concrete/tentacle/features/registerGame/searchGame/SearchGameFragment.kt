package br.com.concrete.tentacle.features.registerGame.searchGame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseSearchFragment
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.ViewStateModel
import kotlinx.android.synthetic.main.fragment_search_game.*
import kotlinx.android.synthetic.main.list_custom.view.*
import kotlinx.android.synthetic.main.list_error_custom.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchGameFragment : BaseSearchFragment(), View.OnClickListener {

    private val gameViewModel: SearchGameViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_game, container, false)
    }

    override fun initView() {
        enableProgress(false)
    }

    override fun getToolbarTitle(): Int = R.string.toolbar_title_search_game

    override fun initViewModel() {
        gameViewModel.getSearchGame().observe(this, Observer { gameModel ->
            when (gameModel.status) {
                ViewStateModel.Status.LOADING -> {
                    enableProgress(true)
                    enableCustomError(false)
                }
                ViewStateModel.Status.SUCCESS -> {
                    enableProgress(false)
                    showList(gameModel.model)
                }
                ViewStateModel.Status.ERROR -> {
                    enableProgress(false)
                    showError(gameModel.errors)
                }
            }
        })

        gameViewModel.getRegisteredGame().observe(this, Observer { game ->
            when (game.status) {
                ViewStateModel.Status.LOADING -> {
                    enableCustomError(false)
                    enableLoadingButton(true)
                    enableProgress(true)
                }
                ViewStateModel.Status.SUCCESS -> {
                    enableCustomError(false)
                    enableLoadingButton(false)
                    enableProgress(false)
                    navigateToRegisterPlatform(game.model!!)
                }
                ViewStateModel.Status.ERROR -> {
                    showError(game.errors)
                    enableLoadingButton(false)
                    enableProgress(false)
                }
            }
        })
    }

    private fun enableLoadingButton(isEnable: Boolean) {
        listCustom.buttonAction.isLoading(isEnable)
    }

    private fun showList(model: ArrayList<Game>?) {
        model?.isNotEmpty()?.let {
            val recyclerViewAdapter =
                BaseAdapter(model,
                    R.layout.item_game, {
                        SearchGameViewHolder(it)
                    }, { holder, element ->
                        SearchGameViewHolder.callBack(holder = holder, game = element, listener = { gameSelected ->
                            navigateToRegisterPlatform(gameSelected)
                        })
                    })
            listCustom.recyclerListView.adapter = recyclerViewAdapter
            listCustom.updateUi(model)
        } ?: listCustom.updateUi(model)
    }

    override fun getSearchGame(searchGame: String) {
        gameViewModel.searchGame(searchGame)
    }

    override fun initListener() {
        listCustom.recyclerListError.buttonNameError.setOnClickListener(this)
    }

    override fun initRecyclerView() {
        listCustom.recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        listCustom.recyclerListView.layoutManager = layoutManager
    }

    override fun onMenuItemActionExpand(item: MenuItem?) = true

    override fun onMenuItemActionCollapse(item: MenuItem?) = true

    override fun titleToolbar() = getString(R.string.add_new_game)

    override fun onClick(v: View?) {
        if (v?.id == R.id.buttonNameError) registerNewGame()
    }

    private fun registerNewGame() {
        gameViewModel.registerNewGame(title = getQuerySearchView())
    }

    private fun enableProgress(isEnable: Boolean) {
        listCustom.setLoading(isEnable)
    }

    private fun enableCustomError(isEnable: Boolean) {
        listCustom.visibleCustomError(isEnable)
    }

    private fun navigateToRegisterPlatform(game: Game) {
        val directions = SearchGameFragmentDirections.NavigateToRegisterPlatform(game)
        findNavController().navigate(directions)
    }
}
