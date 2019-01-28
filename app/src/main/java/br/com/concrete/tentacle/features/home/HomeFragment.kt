package br.com.concrete.tentacle.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.Game

class HomeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

//        recyclerListView.setHasFixedSize(true)
//        val layoutManager = LinearLayoutManager(context)
//        recyclerListView.layoutManager = layoutManager
//
//        //TODO only test, replace to right logic
//        val recyclerViewAdapter = BaseAdapter(
//            ArrayList<Game>(),
//            R.layout.item_home_game,
//            { view ->
//                HomeViewHolder(view)
//            }, { holder, element ->
//                HomeViewHolder.callBack(holder = holder, element = element)
//            })
//
//        recyclerListView.adapter = recyclerViewAdapter

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun getToolbarTitle(): Int {
        return R.string.toolbar_title_home
    }
}