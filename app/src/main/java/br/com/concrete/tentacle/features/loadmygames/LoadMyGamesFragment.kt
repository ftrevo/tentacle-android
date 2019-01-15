package br.com.concrete.tentacle.features.loadmygames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import kotlinx.android.synthetic.main.fragment_game_list.view.*
import kotlinx.android.synthetic.main.list_custom.view.*

class LoadMyGamesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)

        view.list.recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        view.list.recyclerListView.layoutManager = layoutManager

        var medias = ArrayList<Media>()
        //TODO SHOULD BE REPLACED BY OBSERVABLE

        val recyclerViewAdapter = BaseAdapter<Media>(
            medias,
            R.layout.item_game,
            {
                LoadMyGamesViewHolder(it)
            }, { holder, element ->
                LoadMyGamesViewHolder.callBack(holder = holder, element = element)
            })

        view.list.recyclerListView.adapter = recyclerViewAdapter
        view.list.updateUi<Media>(medias)
        return view
    }
}
