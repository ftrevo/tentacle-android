package br.com.concrete.tentacle.features.myreservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import kotlinx.android.synthetic.main.fragment_my_reservation.listMyReservations
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_custom.view.recyclerListView
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError
import org.koin.android.viewmodel.ext.android.viewModel

class MyReservationFragment : BaseFragment() {

    private val myReservationViewModel: MyReservationViewModel by viewModel()

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
                    loadRecyclerView(base.model)
                }
                ViewStateModel.Status.ERROR -> {
                    callError(base)
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

    private fun loadRecyclerView(model: ArrayList<LoanResponse>?) {
        model?.let {
            val recyclerViewAdapter = BaseAdapter(
                model,
                R.layout.item_my_reservation,
                { view ->
                    MyReservationViewHolder(view)
                }, { holder, element ->
                    MyReservationViewHolder.callBack(holder, element) {
                    }
                })
            listMyReservations.recyclerListView.adapter = recyclerViewAdapter
        }

        listMyReservations.updateUi(model)
        listMyReservations.setLoading(false)
    }

    private fun initView() {
        listMyReservations.recyclerListView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        listMyReservations.recyclerListView.layoutManager = layoutManager

        listMyReservations.recyclerListError.buttonNameError.setOnClickListener {
            callback?.changeBottomBar(R.id.action_library, R.id.navigate_to_library)
        }
    }
}
