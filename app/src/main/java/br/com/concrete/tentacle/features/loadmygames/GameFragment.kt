package br.com.concrete.tentacle.features.loadmygames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.concrete.tentacle.base.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.data.models.Game
import kotlinx.android.synthetic.main.fragment_game_list.view.*
import kotlinx.android.synthetic.main.list_custom.*

class GameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_game_list, container, false)

        recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerListView.layoutManager = layoutManager

        val recyclerViewAdapter = BaseAdapter<Game>(ArrayList<Game>(), 0, {
            GameViewHolder(it)
        },{holder, position ->
            //TODO SET UP COMPONENTS
        })
        recyclerListView.adapter = recyclerViewAdapter

        v.list.updateUi<List<Game>>(null)
        return v
    }
}
