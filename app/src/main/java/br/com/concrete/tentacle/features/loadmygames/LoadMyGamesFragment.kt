package br.com.concrete.tentacle.features.loadmygames

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.custom.ListCustom
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.features.lendgame.LendGameActivity
import br.com.concrete.tentacle.features.registerGame.RegisterGameHostActivity
import br.com.concrete.tentacle.utils.TIME_PROGRESS_LOAD
import kotlinx.android.synthetic.main.fragment_game_list.list
import kotlinx.android.synthetic.main.list_custom.recyclerListView
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.buttonAction
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import org.koin.android.viewmodel.ext.android.viewModel

private const val REQUEST_CODE = 1

class LoadMyGamesFragment : BaseFragment(), ListCustom.OnScrollListener {

    private val viewModelLoadMyGames: LoadMyGamesViewModel by viewModel()
    private var recyclerViewAdapter: BaseAdapter<Media>? = null
    private var lMedia = ArrayList<Media?>()
    private var count = 0
    private var loadMoreItems = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initObserver() {
        list.recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        list.recyclerListView.layoutManager = layoutManager

        viewModelLoadMyGames.getMyGamesPage().observe(this, Observer { stateModel ->
            stateModel.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        val mediaResponse = it.model
                        val medias = mediaResponse?.list as ArrayList<Media>
                        count = mediaResponse.count

                        medias.let {
                            Handler().postDelayed({
                                recyclerViewAdapter?.notifyItemInserted(lMedia.size - 1)
                                lMedia.removeAt(lMedia.size - 1)
                                recyclerViewAdapter?.notifyItemRemoved(lMedia.size - 1)
                                lMedia.addAll(medias)

                                if (lMedia.size == count) {
                                    lMedia.add(Media.getEmptyMedia())
                                }

                                loadMoreItems = true
                                list?.buttonAction?.visibility = View.VISIBLE
                                recyclerViewAdapter?.setNewList(lMedia)
                            }, TIME_PROGRESS_LOAD)
                        }
                    }
                    ViewStateModel.Status.LOADING -> {
                    }
                    ViewStateModel.Status.ERROR -> {
                        loadMoreItems = false
                    }
                }
            }
        })

        viewModelLoadMyGames.getMyGames().observe(this, Observer { stateModel ->
            stateModel.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        val mediaResponse = it.model
                        val medias = mediaResponse?.list as ArrayList<Media?>
                        count = mediaResponse.count
                        lMedia.clear()
                        medias.let {
                            if (lMedia.isEmpty()) {
                                lMedia.addAll(medias)
                                recyclerViewAdapter = BaseAdapter(
                                    lMedia,
                                    R.layout.item_game,
                                    { view ->
                                        LoadMyGamesViewHolder(view)
                                    }, { holder, element ->
                                        LoadMyGamesViewHolder.callBack(
                                            holder = holder,
                                            el = element,
                                            listener = { media ->
                                                callActivity(media)
                                            })
                                    })

                                recyclerListView.layoutManager = LinearLayoutManager(context)
                                recyclerListView.setItemViewCacheSize(medias.size)
                                list.recyclerListView.adapter = recyclerViewAdapter

                                list.updateUi(lMedia)
                            }
                        }
                        list.setLoading(false)
                    }

                    ViewStateModel.Status.ERROR -> {
                        it.errors?.let {
                            list.setErrorMessage(R.string.load_games_error_not_know)
                            list.setButtonTextError(R.string.load_again)
                            list.setActionError {
                                viewModelLoadMyGames.loadGamePage()
                            }
                        }
                        list.updateUi<Media>(null)
                        list.setLoading(false)
                    }
                    ViewStateModel.Status.LOADING -> {
                        list.setLoading(true)
                    }
                }
            }
        })

        lifecycle.addObserver(viewModelLoadMyGames)
    }

    private fun callActivity(media: Media) {
        val bundle = Bundle()
        bundle.putString(LendGameActivity.MEDIA_ID_EXTRA, media._id)
        activity?.launchActivity<LendGameActivity>(extras = bundle, animation = ActivityAnimation.TRANSLATE_UP)
    }

    private fun init() {
        initObserver()
        list.setOnScrollListener(this)
        list.recyclerListError.buttonNameError.setOnClickListener {
            showRegisterGame()
        }

        list.buttonAction.setOnClickListener {
            showRegisterGame()
        }
    }

    private fun showRegisterGame() {
        startActivityForResult(Intent(context, RegisterGameHostActivity::class.java), REQUEST_CODE)
    }

    override fun getToolbarTitle(): Int {
        return R.string.toolbar_title_my_games
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun count() = count

    override fun sizeElements() = lMedia.size

    override fun loadMore() {
        viewModelLoadMyGames.loadGamePage()
        lMedia.add(null)
        loadMoreItems = false

        recyclerViewAdapter?.notifyItemInserted(lMedia.size - 1)
        lMedia.addAll(ArrayList<Media>())
        recyclerViewAdapter?.setNewList(lMedia)
    }

    override fun loadPage(): Boolean = loadMoreItems
}
