package br.com.concrete.tentacle.features.myreservations

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseActivity
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.custom.ListCustom
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.extensions.ActivityAnimation
import br.com.concrete.tentacle.extensions.launchActivity
import br.com.concrete.tentacle.features.HostActivity
import br.com.concrete.tentacle.features.myreservations.detail.MyReservationActivity
import br.com.concrete.tentacle.utils.TIME_PROGRESS_LOAD
import kotlinx.android.synthetic.main.fragment_my_reservation.listMyReservations
import kotlinx.android.synthetic.main.list_custom.view.buttonAction
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import org.koin.android.viewmodel.ext.android.viewModel

class MyReservationFragment : BaseFragment(), ListCustom.OnScrollListener {

    private val myReservationViewModel: MyReservationViewModel by viewModel()
    private val myReservationList = ArrayList<LoanResponse?>()

    private var recyclerViewAdapter: BaseAdapter<LoanResponse>? = null

    private var count = 0
    private var loadMoreItems = true

    override fun getToolbarTitle(): Int {
        return R.string.toolbar_title_my_reservations
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
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
                            model = base.model?.list as ArrayList<LoanResponse>,
                            status = base.status
                        )
                    )
                }
            }
        })

        myReservationViewModel.getMyReservationsPage().observe(this, Observer { base ->
            base.getContentIfNotHandler()?.let {
                when(it.status) {
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
                        MyReservationViewHolder.callBack(holder, element) {
                            holder.itemView.setOnClickListener {
                                val bundle = Bundle()
                                bundle.putString(MyReservationActivity.LOAN_EXTRA_ID, element._id)
                                activity?.launchActivity<MyReservationActivity>(extras = bundle, animation = ActivityAnimation.TRANSLATE_UP)
                            }
                        }
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
            callback?.changeBottomBar(R.id.action_library, R.id.navigate_to_library)
        }

        (activity as BaseActivity).setupToolbar(R.string.toolbar_title_my_reservations, R.drawable.ic_logo_actionbar)
    }

    override fun count() = count

    override fun sizeElements() = myReservationList.size

    override fun loadMore() {
        myReservationViewModel.loadMyReservationsPage()
        myReservationList.add(null)
        loadMoreItems = false

        recyclerViewAdapter?.notifyItemInserted(myReservationList.size - 1)
        myReservationList.addAll(ArrayList<LoanResponse>())
        recyclerViewAdapter?.setNewList(myReservationList)
    }

    override fun loadPage() = loadMoreItems
}
