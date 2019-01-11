package br.com.concrete.tentacle.features.registerGame


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseSearchFragment
import br.com.concrete.tentacle.data.models.ViewStateModel
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
        newText?.let {
            text-> gameViewModel.searchGame(text)
        }
        return false
    }

    override fun init() {
        gameViewModel.getSearchGame().observe(this, Observer {stateModel->
            when(stateModel.status) {
                ViewStateModel.Status.LOADING-> {

                }
                ViewStateModel.Status.SUCCESS-> {
                    Log.i("SIZE ", stateModel.model?.size.toString())
                }
                ViewStateModel.Status.ERROR-> {

                }
            }

        })
    }

    override fun onMenuItemActionExpand(item: MenuItem?) = true

    override fun onMenuItemActionCollapse(item: MenuItem?) = true
}
