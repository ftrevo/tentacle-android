package br.com.concrete.tentacle.features.loadmygames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.ViewStateModel
import kotlinx.android.synthetic.main.fragment_game_list.view.list
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import org.koin.android.viewmodel.ext.android.viewModel

class LoadMyGamesFragment : BaseFragment() {

    private val viewModelLoadMyGames: LoadMyGamesViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)

        /*viewModelLoadMyGames.getMyGames().observe(this, androidx.lifecycle.Observer{ stateModel ->

            when(stateModel.status){
                ViewStateModel.Status.SUCCESS ->{
                     stateModel.model?.let { medias ->
                        view.list.recyclerListView.setHasFixedSize(true)
                        val layoutManager = LinearLayoutManager(context)
                        view.list.recyclerListView.layoutManager = layoutManager

                        val recyclerViewAdapter = BaseAdapter(
                            medias,
                            R.layout.item_game,
                            { view ->
                                LoadMyGamesViewHolder(view)
                            }, { holder, element ->
                                LoadMyGamesViewHolder.callBack(holder = holder, element = element)
                            })

                        view.list.recyclerListView.adapter = recyclerViewAdapter
                        view.list.updateUi(medias)
                    }
                }

                ViewStateModel.Status.ERROR -> {
                    stateModel.errors?.let {
                        showError(it)
                    }
                }
            }
        })*/

        return view
    }
}
