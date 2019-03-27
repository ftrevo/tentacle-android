package br.com.concrete.tentacle.features.library

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.base.TIME_OUT
import br.com.concrete.tentacle.base.MINIMAL_CHARACTER
import br.com.concrete.tentacle.custom.ListCustom
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.filter.SubItem
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.features.filter.FilterDialogFragment
import br.com.concrete.tentacle.features.library.loan.LoanActivity
import br.com.concrete.tentacle.utils.MOCK_FILTER_LIBRARY
import br.com.concrete.tentacle.utils.QueryUtils
import br.com.concrete.tentacle.utils.TIME_PROGRESS_LOAD
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.fragment_library.list
import kotlinx.android.synthetic.main.list_custom.recyclerListView
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class LibraryFragment : BaseFragment(), FilterDialogFragment.OnFilterListener, ListCustom.OnScrollListener {

    private val viewModelLibrary: LibraryViewModel by viewModel()
    private var recyclerViewAdapter: BaseAdapter<Library>? = null
    private var libraries = ArrayList<Library?>()

    private val selectedFilterItems = ArrayList<SubItem>()
    private var queryParameters: QueryParameters? = null

    private lateinit var searchView: SearchView

    private lateinit var editText: EditText

    private var count = 0
    private var loadMoreItems = true

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
        list.setOnScrollListener(this)

        list.recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        list.recyclerListView.layoutManager = layoutManager

        list.recyclerListError.buttonNameError.setOnClickListener {
            callback?.changeBottomBar(R.id.action_games, R.id.navigate_to_my_games)
        }
    }

    private fun initObserver() {
        viewModelLibrary.getLibrary().observe(this, Observer { stateModel ->
            stateModel.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        it.model?.let { list ->
                            libraries.clear()

                            val libraryResponse = it.model
                            val libs = libraryResponse.list as ArrayList<Library?>
                            count = libraryResponse.count

                            libs.let {
                                recyclerViewAdapter = BaseAdapter(
                                    libs,
                                    R.layout.library_item_layout,
                                    { view ->
                                        LibraryViewHolder(view, viewStateOpen = false) { library ->
                                            val extras = Bundle()
                                            extras.putString(LoanActivity.ID_LIBRARY_EXTRA, library._id)
                                            activity?.launchActivity<LoanActivity>(
                                                extras = extras,
                                                animation = ActivityAnimation.TRANSLATE_UP
                                            )
                                        }
                                    }, { holder, element ->
                                        LibraryViewHolder.callBack(
                                            holder = holder,
                                            element = element,
                                            selectedFilters = selectedFilterItems
                                        )
                                    })

                                recyclerListView.layoutManager = LinearLayoutManager(context)
                                recyclerListView.setItemViewCacheSize(libraries.size)
                                recyclerListView.adapter = recyclerViewAdapter

                                libraries = libs
                            }
                        }
                        list.updateUi(libraries, it.filtering)
                        list.setLoading(false)
                    }
                    ViewStateModel.Status.LOADING -> {
                        list.setLoading(true)
                    }

                    ViewStateModel.Status.ERROR -> {
                        it.errors?.let {
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
            }
        })

        viewModelLibrary.getLibraryMore().observe(this, Observer { stateModel ->
            stateModel.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        val libraryResponse = it.model
                        val libs = libraryResponse?.list as ArrayList<Library?>
                        count = libraryResponse.count

                        Handler().postDelayed({
                            recyclerViewAdapter?.notifyItemInserted(libraries.size - 1)
                            libraries.removeAt(libraries.size - 1)
                            recyclerViewAdapter?.notifyItemRemoved(libraries.size - 1)
                            libraries.addAll(libs)
                            loadMoreItems = true
                            recyclerViewAdapter?.setNewList(libraries)
                        }, TIME_PROGRESS_LOAD)
                    }
                    ViewStateModel.Status.LOADING -> {}
                    ViewStateModel.Status.ERROR -> {
                        loadMoreItems = true
                    }
                }
            }
        })

        lifecycle.addObserver(viewModelLibrary)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        setupOptionMenu(menu, inflater)
    }

    private fun setupOptionMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter, menu)
        if (menu.findItem(R.id.action_search).actionView is SearchView) {
            initSearchView(menu.findItem(R.id.action_search).actionView as SearchView)
        }
    }

    @SuppressLint("CheckResult")
    private fun initSearchView(menu: SearchView) {
        searchView = menu

        context?.let {
            searchView.queryHint = it.getString(R.string.search)
        }

        val closeButton = searchView.findViewById<ImageView>(R.id.search_close_btn)
        val button = searchView.findViewById<ImageView>(R.id.search_button)
        editText = searchView.findViewById(R.id.search_src_text)

        button.setOnClickListener {
            (activity as BaseActivity).setupToolbar(false)
            searchView.isIconified = false
        }

        closeButton.setOnClickListener {
            editText.setText("")
            (activity as BaseActivity).setupToolbar(R.drawable.ic_logo_actionbar)
            selectedFilterItems.clear()
            activity?.invalidateOptionsMenu()
            queryParameters = null

            searchView.isIconified = true
            searchView.setIconifiedByDefault(true)
            viewModelLibrary.loadLibrary(null, null)
        }

        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }
            })
        })
            .map { text -> text.toLowerCase().trim() }
            .debounce(TIME_OUT, TimeUnit.MILLISECONDS)
            .filter { text -> validateSearch(text) }
            .subscribe { text -> getSearchGame(text) }
    }

    private fun getSearchGame(text: String?) {
        viewModelLibrary.loadLibrary(queryParameters, text, true)
    }

    private fun validateSearch(search: String) = search.trim().length >= MINIMAL_CHARACTER

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.filterMenuId).let {
            val iconRes = if (selectedFilterItems.isEmpty()) R.drawable.ic_filter_off else R.drawable.ic_filter_on
            it.setIcon(iconRes)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.filterMenuId) {
            FilterDialogFragment.showDialog(this, selectedFilterItems, MOCK_FILTER_LIBRARY)
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onFilterListener(filters: List<SubItem>) {
        activity?.invalidateOptionsMenu()
        selectedFilterItems.clear()
        selectedFilterItems.addAll(filters)

        if (filters.isEmpty()) {
            editText.setText("")
            searchView.setIconifiedByDefault(false)
            (activity as BaseActivity).setupToolbar(false)
        }

        queryParameters = QueryUtils.assemblefilterQuery(selectedFilterItems)
        updateListBeforeFilter()
        viewModelLibrary.loadLibrary(
            queryParameters,
            if (editText.text.toString().isEmpty())
                null else editText.text.toString()
        )

        searchView.setIconifiedByDefault(true)
    }

    private fun updateListBeforeFilter() {
        viewModelLibrary.initPage()
        libraries = ArrayList()
        recyclerViewAdapter?.notifyDataSetChanged()
    }

    override fun count() = count

    override fun sizeElements(): Int = libraries.size

    override fun loadMore() {
        viewModelLibrary.loadLibraryMore(
            queryParameters,
            if (searchView.query.toString() == "") null else searchView.query.toString(),
            true
        )
        libraries.add(null)
        loadMoreItems = false

        recyclerViewAdapter?.notifyItemInserted(libraries.size - 1)
        libraries.addAll(ArrayList<Library>())
        recyclerViewAdapter?.setNewList(libraries)
    }

    override fun loadPage(): Boolean = loadMoreItems
}