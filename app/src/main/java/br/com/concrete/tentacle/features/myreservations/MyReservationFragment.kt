package br.com.concrete.tentacle.features.myreservations

import android.content.DialogInterface
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
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.filter.SubItem
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.features.filter.FilterDialogFragment
import br.com.concrete.tentacle.features.myreservations.detail.MyReservationActivity
import br.com.concrete.tentacle.utils.DialogUtils
import br.com.concrete.tentacle.utils.MOCK_FILTER_MY_GAMES_MY_RESERVATION
import br.com.concrete.tentacle.utils.QueryUtils
import br.com.concrete.tentacle.utils.TIME_PROGRESS_LOAD
import kotlinx.android.synthetic.main.fragment_my_reservation.listMyReservations
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import kotlinx.android.synthetic.main.progress_include.view.progressBarList
import org.koin.android.viewmodel.ext.android.viewModel

class MyReservationFragment : BaseFragment(), ListCustom.OnScrollListener, FilterDialogFragment.OnFilterListener {

    private val myReservationViewModel: MyReservationViewModel by viewModel()
    private var myReservationList = ArrayList<LoanResponse?>()

    private var recyclerViewAdapter: BaseAdapter<LoanResponse>? = null

    private var count = 0
    private var loadMoreItems = true

    private var queryParameters: QueryParameters? = null
    private val selectedFilterItems = ArrayList<SubItem>()

    override fun getToolbarTitle(): Int {
        return R.string.toolbar_title_my_reservations
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initView()
        initObservable()
    }

    private fun initObservable() {
        myReservationViewModel.getMyReservations().observe(this, Observer { base ->
            when (base.status) {
                ViewStateModel.Status.SUCCESS -> {
                    base.model?.let { loanResponseList ->
                        count = loanResponseList.count
                        myReservationList.clear()

                        myReservationList.addAll(loanResponseList.list)
                        if (myReservationList.isEmpty()) {
                            loadEmptyState()
                        } else {
                            loadRecyclerView(myReservationList)
                        }
                    }
                }
                ViewStateModel.Status.ERROR -> {
                    callError(
                        ViewStateModel(
                            status = base.status
                        )
                    )
                }
            }
        })

        myReservationViewModel.getMyReservationsPage().observe(this, Observer { base ->
            base.getContentIfNotHandler()?.let {
                when (it.status) {
                    ViewStateModel.Status.SUCCESS -> {
                        val loansResponse = it.model
                        val loansList = loansResponse?.list as ArrayList<LoanResponse>
                        count = loansResponse.count

                        loansResponse.let {
                            Handler().postDelayed({
                                recyclerViewAdapter?.notifyItemInserted(myReservationList.size - 1)
                                myReservationList.removeAt(myReservationList.size - 1)
                                recyclerViewAdapter?.notifyItemRemoved(myReservationList.size - 1)
                                myReservationList.addAll(loansList)

                                loadMoreItems = true
                                recyclerViewAdapter?.setNewList(myReservationList)
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

        myReservationViewModel.getStateDeleteLoan().observe(this, Observer { base ->
            when (base.status) {
                ViewStateModel.Status.LOADING -> listMyReservations.setLoading(true)
                ViewStateModel.Status.SUCCESS -> {
                    listMyReservations.setLoading(false)
                    myReservationList.removeAt(MyReservationViewHolder.itemRemove)
                    recyclerViewAdapter?.notifyItemRemoved(MyReservationViewHolder.itemRemove)
                    listMyReservations.progressBarList.visibility = View.GONE
                }
                ViewStateModel.Status.ERROR -> {
                    listMyReservations.setLoading(false)
                    showError(base.errors, getString(R.string.unknow_error))
                }
            }
        })

        lifecycle.addObserver(myReservationViewModel)
    }

    private fun callError(base: ViewStateModel<ArrayList<LoanResponse>>) {
        base.errors?.let {
            listMyReservations.setErrorMessage(R.string.load_reservations_error_not_know)
            listMyReservations.setButtonTextError(R.string.load_again)
            listMyReservations.setActionError {
                myReservationViewModel.loadMyReservations()
            }
        }
        listMyReservations.updateUi<LoanResponse>(null)
        listMyReservations.setLoading(false)
        listMyReservations.buttonNameError.setButtonName(getString(R.string.load_again))
    }

    private fun loadRecyclerView(model: ArrayList<LoanResponse?>?) {
        model?.let {
            recyclerViewAdapter = BaseAdapter(
                model,
                R.layout.item_my_reservation,
                { view ->
                    MyReservationViewHolder(view)
                }, { holder, element ->
                    element?.let {
                        MyReservationViewHolder.callBack(holder, element, {
                            holder.itemView.setOnClickListener {
                                val bundle = Bundle()
                                bundle.putString(MyReservationActivity.LOAN_EXTRA_ID, element._id)
                                activity?.launchActivity<MyReservationActivity>(
                                    extras = bundle,
                                    animation = ActivityAnimation.TRANSLATE_UP
                                )
                            }
                        }, {
                            showDialogDelete(it)
                        })
                    }
                })

            listMyReservations.recyclerListView.setItemViewCacheSize(model.size)
            listMyReservations.recyclerListView.adapter = recyclerViewAdapter
        }

        listMyReservations.updateUi(model)
        listMyReservations.setLoading(false)
    }

    private fun loadEmptyState() {
        listMyReservations.setErrorMessage(R.string.load_reservations_empty_list)
        listMyReservations.setButtonNameAction(R.string.btn_reservation_error_empty)

        listMyReservations.updateUi(myReservationList)
        listMyReservations.setLoading(false)
    }

    private fun initView() {
        listMyReservations.recyclerListView.setHasFixedSize(true)
        listMyReservations.setOnScrollListener(this)
        val layoutManager = LinearLayoutManager(context)
        listMyReservations.recyclerListView.layoutManager = layoutManager

        listMyReservations.recyclerListError.buttonNameError.setOnClickListener {
            myReservationViewModel.loadMyReservations()
        }

        (activity as BaseActivity).setupToolbar(R.string.toolbar_title_my_reservations, R.drawable.ic_menu)
    }

    override fun count() = count

    override fun sizeElements() = myReservationList.size

    override fun loadMore() {
        myReservationViewModel.loadMyReservationsPage(queryParameters)
        myReservationList.add(null)
        loadMoreItems = false

        recyclerViewAdapter?.notifyItemInserted(myReservationList.size - 1)
        myReservationList.addAll(ArrayList<LoanResponse>())
        recyclerViewAdapter?.setNewList(myReservationList)
    }

    override fun loadPage() = loadMoreItems

    private fun showDialogDelete(loanResponse: LoanResponse) {
        activity?.let {
            val gameName = String.format(getString(R.string.delete_dialog_message_reservation), loanResponse.game.name)
            DialogUtils.showDialog(
                contentView = R.layout.custom_dialog_error,
                context = it,
                title = getString(R.string.delete_dialog_title_reservation),
                message = gameName,
                positiveText = getString(R.string.remove),
                positiveListener = DialogInterface.OnClickListener { _, _ ->
                    myReservationViewModel.deleteLoan(loanResponse._id)
                },
                negativeText = getString(R.string.not_delete),
                negativeListener = DialogInterface.OnClickListener { _, _ ->
                    listMyReservations.setLoading(false)
                }
            )
        }
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
            FilterDialogFragment.showDialog(this, selectedFilterItems, MOCK_FILTER_MY_GAMES_MY_RESERVATION)
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onFilterListener(filters: List<SubItem>) {
        activity?.invalidateOptionsMenu()
        selectedFilterItems.clear()
        selectedFilterItems.addAll(filters)

        if (filters.isEmpty()) {
            (activity as BaseActivity).setupToolbar(false)
        }

        queryParameters = QueryUtils.assemblefilterQuery(selectedFilterItems)
        updateListBeforeFilter()
        myReservationViewModel.myReservations(queryParameters)
    }

    private fun updateListBeforeFilter() {
        myReservationViewModel.resetPage()
        myReservationList = ArrayList()
        recyclerViewAdapter?.notifyDataSetChanged()
    }
}
