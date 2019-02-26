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
import kotlinx.android.synthetic.main.fragment_search_game.listCustom
import kotlinx.android.synthetic.main.list_custom.view.buttonAction
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import org.koin.android.viewmodel.ext.android.viewModel

class SearchGameFragment : BaseSearchFragment(), View.OnClickListener {

    private val gameViewModel: SearchGameViewModel by viewModel()

    private var tempList: ArrayList<Game?> = ArrayList()

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
                    showList(gameModel.model!!)
                }
                ViewStateModel.Status.ERROR -> {
                    enableProgress(false)
                    loadMessageErrorLoading(gameModel)
                }
            }
        })

        gameViewModel.getRegisteredGame().observe(this, Observer { it ->
            it.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.LOADING -> {
                        enableCustomError(false)
                        enableLoadingButton(true)
                        enableProgress(true)
                    }
                    ViewStateModel.Status.SUCCESS -> {
                        enableCustomError(false)
                        enableLoadingButton(false)
                        enableProgress(false)
                        navigateToRegisterPlatform(it.model!!)
                    }
                    ViewStateModel.Status.ERROR -> {
                        showError(it.errors)
                        enableLoadingButton(false)
                        enableProgress(false)
                    }
                }
            }
        })
    }

    private fun loadMessageErrorLoading(gameModel: ViewStateModel<ArrayList<Game?>>) {
        gameModel.errors?.let {
            listCustom.setErrorMessage(R.string.load_games_error_not_know)
            listCustom.setButtonTextError(R.string.load_again)
            listCustom.setActionError {
                getSearchGame(getQuerySearchView())
            }
        }
        listCustom.updateUi<Game>(null)
        listCustom.setLoading(false)
    }

    private fun enableLoadingButton(isEnable: Boolean) {
        listCustom.buttonAction.isLoading(isEnable)
    }

    private fun showList(model: ArrayList<Game?>) {
        if (model?.isNotEmpty()!!) {
            listCustom.setButtonNameAction(R.string.did_not_find_what_you_wanted)
            fillRecyclerView(model)
        } else {
            listCustom.setButtonNameAction(R.string.add_new_game)
            listCustom.updateUi(model)
            if (tempList.isNotEmpty()) {
                tempList = ArrayList()
            }
        }
    }

    private fun fillRecyclerView(model: ArrayList<Game?>) {
        val recyclerViewAdapter =
            BaseAdapter(model,
                R.layout.item_game_search, {
                    it?.let { it1 -> SearchGameViewHolder(it1) }
                }, { holder, element ->
                    SearchGameViewHolder.callBack(holder = holder, game = element, listener = { gameSelected ->
                        navigateToRegisterPlatform(gameSelected)
                    })
                })
        listCustom.recyclerListView.setItemViewCacheSize(model.size)
        listCustom.recyclerListView.adapter = recyclerViewAdapter
        listCustom.updateUi(model)

        tempList = model
    }

    override fun getSearchGame(searchGame: String) {
        gameViewModel.searchGame(searchGame)
    }

    override fun initListener() {
        listCustom.recyclerListError.buttonNameError.setOnClickListener(this)
        listCustom.buttonAction.setOnClickListener(this)
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
        when (v?.id) {
            R.id.buttonNameError -> registerNewGame()
            R.id.buttonAction -> registerNewGame()
        }
    }

    private fun registerNewGame() {
        if (validateSearch(getQuerySearchView())) gameViewModel.registerNewGame(name = getQuerySearchView())
        else callSnackBar(getString(R.string.field_search_no_empty))
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

    override fun clearListGame() {
        listCustom.recyclerListView.visibility = View.GONE
        listCustom.buttonAction.visibility = View.GONE
        listCustom.recyclerListError.visibility = View.GONE
    }
}
