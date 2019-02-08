package br.com.concrete.tentacle.features.loadmygames

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import br.com.concrete.tentacle.custom.ListCustom
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.registerGame.RegisterGameHostActivity
import kotlinx.android.synthetic.main.fragment_game_list.list
import kotlinx.android.synthetic.main.list_custom.view.buttonAction
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import org.koin.android.viewmodel.ext.android.viewModel


private const val REQUEST_CODE = 1

class LoadMyGamesFragment : BaseFragment(), ListCustom.OnScrollListener {

    private val viewModelLoadMyGames: LoadMyGamesViewModel by viewModel()

    private var recyclerViewAdapter = BaseAdapter<Media>()
    private var lMedia = ArrayList<Media?>()
    var count = 0

    var loadMore: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObserver() {
        list.recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        list.recyclerListView.layoutManager = layoutManager

        viewModelLoadMyGames.getMyGames().observe(this, Observer { stateModel ->
            stateModel.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        val mediaResponse = it.model
                        val medias = mediaResponse?.list as ArrayList<Media?>
                        count = mediaResponse.count
                        medias.let {
                            if (lMedia.isEmpty()) {
                                recyclerViewAdapter = BaseAdapter(
                                    medias,
                                    R.layout.item_game,
                                    { view ->
                                        LoadMyGamesViewHolder(view!!)
                                    }, { holder, element ->
                                        LoadMyGamesViewHolder.callBack(holder = holder, element = element)
                                    })
                                list.recyclerListView.adapter = recyclerViewAdapter
                                lMedia = medias
                                list.updateUi(medias)
                                list.setLoading(false)
                            } else {
                                lMedia.add(null)
                                recyclerViewAdapter.notifyItemInserted(lMedia.size - 1)

                                Handler().postDelayed({
                                    lMedia.removeAt(lMedia.size - 1)
                                    recyclerViewAdapter.notifyItemRemoved(lMedia.size)
                                    lMedia.addAll(medias)
                                    loadMore = true
                                    recyclerViewAdapter.setNewList(lMedia)
                                }, 1000)
                            }
                        }
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
                    }
                }
            }
        })

        lifecycle.addObserver(viewModelLoadMyGames)
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

    override fun loadPage(loadPage: Boolean) {
        if (loadPage) {
            viewModelLoadMyGames.loadGamePage()
            loadMore = false
        }
    }

    override var loadPage: Boolean
        get() = loadMore
        set(value) {}

}
