package br.com.concrete.tentacle.features.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import kotlinx.android.synthetic.main.fragment_library.list
import kotlinx.android.synthetic.main.list_custom.recyclerListView
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import br.com.concrete.tentacle.data.models.library.filter.SubItem
import br.com.concrete.tentacle.features.library.filter.FilterDialogFragment
import br.com.concrete.tentacle.utils.QueryUtils
import org.koin.android.viewmodel.ext.android.viewModel

class LibraryFragment : BaseFragment(), FilterDialogFragment.OnFilterListener {

    private val viewModelLibrary: LibraryViewModel by viewModel()
    private var recyclerViewAdapter: BaseAdapter<Library>? = null
    private val libraries = ArrayList<Library>()
    private val selectedFilterItems = ArrayList<SubItem>()
    private lateinit var queryParameters: QueryParameters

    override fun getToolbarTitle() = R.string.toolbar_title_library

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {
        list.recyclerListError.buttonNameError.setOnClickListener {
            callback?.changeBottomBar(R.id.action_games, R.id.navigate_to_my_games)
        }
    }

    private fun initObserver() {
        viewModelLibrary.getLibrary().observe(this, Observer { stateModel ->
            when (stateModel.status) {
                ViewStateModel.Status.SUCCESS -> {
                    stateModel.model?.let {
                        libraries.addAll(it)

                        recyclerViewAdapter = BaseAdapter(
                            libraries,
                            R.layout.library_item_layout,
                            { view ->
                                LibraryViewHolder(view)
                            }, { holder, element ->
                                LibraryViewHolder.callBack(holder = holder, element = element)
                            })

                        recyclerListView.layoutManager = LinearLayoutManager(context)
                        recyclerListView.adapter = recyclerViewAdapter
                    }
                    list.updateUi(libraries)
                    list.setLoading(false)
                }
                ViewStateModel.Status.LOADING -> {
                    list.setLoading(true)
                }

                ViewStateModel.Status.ERROR -> {
                    stateModel.errors?.let {
                        list.setErrorMessage(R.string.load_library_error_not_know)
                        list.setButtonTextError(R.string.load_again)
                        list.setActionError {
                            viewModelLibrary.loadLibrary(queryParameters)
                        }
                    }
                    list.updateUi<Library>(null)
                    list.setLoading(false)
                }
            }
        })
        lifecycle.addObserver(viewModelLibrary)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_filter, menu)
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
            FilterDialogFragment.showDialog(this, selectedFilterItems)
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onFilterListener(filters: List<SubItem>) {
        activity?.invalidateOptionsMenu()
        libraries.clear()
        selectedFilterItems.clear()
        selectedFilterItems.addAll(filters)

        queryParameters = QueryUtils.assemblefilterQuery(selectedFilterItems)
        viewModelLibrary.loadLibrary(queryParameters)
    }
}