package br.com.concrete.tentacle.features.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.interfaces.CallBack
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.ViewStateModel
import kotlinx.android.synthetic.main.fragment_game_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_custom.*
import kotlinx.android.synthetic.main.list_custom.view.*
import kotlinx.android.synthetic.main.list_error_custom.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var callback: CallBack

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservable()
    }

    private fun initObservable() {
        homeViewModel.getHomeGames().observe(this, Observer { base ->
            when (base.status) {
                ViewStateModel.Status.SUCCESS -> {
                    loadRecyclerView(base.model)
                }
                ViewStateModel.Status.ERROR -> {
                    callError(base)
                }
            }
        })

        lifecycle.addObserver(homeViewModel)
    }

    private fun callError(base: ViewStateModel<ArrayList<Game>>) {
        base.errors?.let {
            showError(it)
        }
        listHome.updateUi(base.model)
        listHome.setLoading(false)
    }

    private fun loadRecyclerView(model: ArrayList<Game>?) {
        model?.let {
            val recyclerViewAdapter = BaseAdapter(
                model,
                R.layout.item_home_game,
                { view ->
                    HomeViewHolder(view)
                }, { holder, element ->
                    HomeViewHolder.callBack(holder = holder, element = element)
                })
            listHome.recyclerListView.adapter = recyclerViewAdapter
        }

        listHome.updateUi(model)
        listHome.setLoading(false)
    }

    private fun initView() {
        listHome.recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        listHome.recyclerListView.layoutManager = layoutManager

        listHome.recyclerListError.buttonNameError.setOnClickListener {
            callback.changeBottomBar(R.id.action_games, R.id.navigate_to_my_games)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CallBack) callback = context
    }

    override fun getToolbarTitle() = R.string.toolbar_title_home
}