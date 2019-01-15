package br.com.concrete.tentacle.features.searchGame


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseSearchFragment
import br.com.concrete.tentacle.data.models.ViewStateModel
import kotlinx.android.synthetic.main.fragment_search_game.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchGameFragment : BaseSearchFragment() {

    private val gameViewModel: SearchGameViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_game, container, false)
    }

    override fun onQueryTextSubmit(query: String?) = true

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null && newText.length > 3) gameViewModel.searchGame(newText)

        return false
    }

    override fun init() {
        gameViewModel.getSearchGame().observe(this, Observer {gameModel->
            when(gameModel.status) {
                ViewStateModel.Status.LOADING-> { }
                ViewStateModel.Status.SUCCESS-> {
                    if (gameModel.model?.isNotEmpty()!!) {

                    } else {
                        newGame.visibility = View.VISIBLE
                    }
                }
                ViewStateModel.Status.ERROR-> {
                    showError(gameModel.errors)
                }
            }
        })

        gameViewModel.getRegisteredGame().observe(this, Observer { game->
            when(game.status) {
                ViewStateModel.Status.LOADING-> { }
                ViewStateModel.Status.SUCCESS-> { }
                ViewStateModel.Status.ERROR-> {
                    showError(game.errors)
                }
            }
        })

        newGame.setOnClickListener {
            gameViewModel.registerNewGame(getQuerySearchView())
        }
    }

    override fun onMenuItemActionExpand(item: MenuItem?) = true

    override fun onMenuItemActionCollapse(item: MenuItem?) = true
}
