package br.com.concrete.tentacle.features.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.features.library.loan.LoanActivity
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.listHome
import kotlinx.android.synthetic.main.list_custom.recyclerListView
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private val DELAY_BETWEEN_TRANSITION = 5000L

    private val homeViewModel: HomeViewModel by viewModel()
    private val publisher: PublishSubject<Boolean> = PublishSubject.create()
    private var isFragmentVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        isFragmentVisible = true
        startAnimation()
    }

    override fun onStop() {
        super.onStop()
        isFragmentVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservable()
    }

    private fun initObservable() {
        homeViewModel.getHomeGames().observe(this, Observer { stateModel ->
            stateModel.getContentIfNotHandler()?.let { state ->
                when (state.status) {
                    ViewStateModel.Status.LOADING -> {}
                    ViewStateModel.Status.SUCCESS -> {
                        state.model?.let { loadRecyclerView(it) }
                    }
                    ViewStateModel.Status.ERROR -> {
                        callError(state)
                    }
                }
            }
        })

        lifecycle.addObserver(homeViewModel)
    }

    private fun callError(base: ViewStateModel<ArrayList<Game?>>) {
        base.errors?.let {
            listHome.setErrorMessage(R.string.load_games_error_not_know)
            listHome.setButtonTextError(R.string.load_again)
            listHome.setActionError {
                homeViewModel.loadHomeGames()
            }
        }
        listHome.updateUi<Game>(null)
        listHome.setLoading(false)
    }

    private fun loadRecyclerView(model: ArrayList<Game?>) {
        model.let {
            val recyclerViewAdapter = BaseAdapter(
                model,
                R.layout.item_home_game,
                { view ->
                    HomeViewHolder(view, publisher)
                }, { holder, element ->
                    HomeViewHolder.callBack(holder = holder, element = element) { game ->
                        val extras = Bundle()
                        extras.putString(LoanActivity.ID_LIBRARY_EXTRA, game._id)
                        activity?.launchActivity<LoanActivity>(
                            extras = extras,
                            animation = ActivityAnimation.TRANSLATE_UP
                        )
                    }
                })
            recyclerListView.adapter = recyclerViewAdapter
        }

        listHome.updateUi(model)
        listHome.setLoading(false)
    }

    private fun startAnimation() {
        if (isFragmentVisible) {
            Handler().postDelayed({
                publisher.onNext(true)
                startAnimation()
            }, DELAY_BETWEEN_TRANSITION)
        }
    }

    private fun initView() {
        recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerListView.layoutManager = layoutManager

        listHome.recyclerListError.buttonNameError.setOnClickListener {
            callback?.changeBottomBar(R.id.action_games, R.id.navigate_to_my_games)
        }
    }

    override fun getToolbarTitle() = R.string.toolbar_title_home
}