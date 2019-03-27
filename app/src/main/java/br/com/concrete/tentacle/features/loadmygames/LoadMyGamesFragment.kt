package br.com.concrete.tentacle.features.loadmygames

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.custom.ListCustom
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.filter.SubItem
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.features.filter.FilterDialogFragment
import br.com.concrete.tentacle.features.lendgame.LendGameActivity
import br.com.concrete.tentacle.features.registerGame.RegisterGameHostActivity
import br.com.concrete.tentacle.utils.DialogUtils
import br.com.concrete.tentacle.utils.MOCK_FILTER_MY_GAMES
import br.com.concrete.tentacle.utils.QueryUtils
import br.com.concrete.tentacle.utils.TIME_PROGRESS_LOAD
import kotlinx.android.synthetic.main.fragment_game_list.list
import kotlinx.android.synthetic.main.list_custom.recyclerListView
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.buttonAction
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import kotlinx.android.synthetic.main.progress_include.view.progressBarList
import org.koin.android.viewmodel.ext.android.viewModel

private const val REQUEST_CODE = 1

class LoadMyGamesFragment : BaseFragment(), ListCustom.OnScrollListener, FilterDialogFragment.OnFilterListener {

    private val viewModelLoadMyGames: LoadMyGamesViewModel by viewModel()
    private var recyclerViewAdapter: BaseAdapter<Media>? = null
    private var lMedia = ArrayList<Media?>()
    private var count = 0
    private var loadMoreItems = true

    private val selectedFilterItems = ArrayList<SubItem>()
    private var queryParameters: QueryParameters? = null

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
        setupOptionMenu(menu, inflater)
    }

    private fun setupOptionMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_load_my_games, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.filterMenuId).let {
            val iconRes = if (selectedFilterItems.isEmpty()) R.drawable.ic_filter_off else R.drawable.ic_filter_on
            it.setIcon(iconRes)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.filterMenuId) {
            FilterDialogFragment.showDialog(this, selectedFilterItems, MOCK_FILTER_MY_GAMES)
            true
        } else super.onOptionsItemSelected(item)
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
                                            },
                                            listenerLongClick = { media ->
                                                showDialogDelete(media)
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
                                viewModelLoadMyGames.loadMyGames(queryParameters)
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

        viewModelLoadMyGames.deleteMedia().observe(this, Observer { stateModel ->
            stateModel.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        lMedia.removeAt(LoadMyGamesViewHolder.itemRemove)
                        recyclerViewAdapter?.notifyItemRemoved(LoadMyGamesViewHolder.itemRemove)
                        list.progressBarList.visibility = View.GONE
                    }
                    ViewStateModel.Status.LOADING -> {
                        list.progressBarList.visibility = View.VISIBLE
                    }
                    ViewStateModel.Status.ERROR -> {
                        showError(it.errors)
                        list.progressBarList.visibility = View.GONE
                    }
                }
            }
        })

        lifecycle.addObserver(viewModelLoadMyGames)
    }

    private fun showDialogDelete(media: Media) {
        val gameName = String.format(getString(R.string.delete_dialog_message), media.game?.name ?: "")
        activity?.let {
            DialogUtils.showDialog(
                context = it,
                title = getString(R.string.delete_dialog_title),
                message = gameName,
                positiveText = getString(R.string.delete),
                positiveListener = DialogInterface.OnClickListener { _, _ ->
                    viewModelLoadMyGames.deleteGame(media._id)
                },
                negativeText = getString(R.string.not_delete)
            )
        }
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
        viewModelLoadMyGames.loadGamePage(queryParameters)
        lMedia.add(null)
        loadMoreItems = false

        recyclerViewAdapter?.notifyItemInserted(lMedia.size - 1)
        lMedia.addAll(ArrayList<Media>())
        recyclerViewAdapter?.setNewList(lMedia)
    }

    override fun loadPage(): Boolean = loadMoreItems

    override fun onFilterListener(filters: List<SubItem>) {
        activity?.invalidateOptionsMenu()
        selectedFilterItems.clear()
        selectedFilterItems.addAll(filters)

        if (filters.isEmpty()) {
            (activity as BaseActivity).setupToolbar(false)
        }

        queryParameters = QueryUtils.assemblefilterQuery(selectedFilterItems)
        updateListBeforeFilter()
        viewModelLoadMyGames.loadMyGames(queryParameters)
    }

    private fun updateListBeforeFilter() {
        viewModelLoadMyGames.resetPage()
        lMedia = ArrayList()
        recyclerViewAdapter?.notifyDataSetChanged()
    }

}
