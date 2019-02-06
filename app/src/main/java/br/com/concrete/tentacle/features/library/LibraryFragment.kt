package br.com.concrete.tentacle.features.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import kotlinx.android.synthetic.main.fragment_game_list.*
import kotlinx.android.synthetic.main.list_custom.*
import org.koin.android.viewmodel.ext.android.viewModel

class LibraryFragment : BaseFragment() {

    private val viewModelLibrary: LibraryViewModel by viewModel()

    override fun getToolbarTitle() = R.string.toolbar_title_library

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObserver() {
        viewModelLibrary.getLibrary().observe(this, Observer { stateModel ->
            val library = stateModel.model
            when (stateModel.status) {
                ViewStateModel.Status.SUCCESS -> {
                    library?.let {

                        val recyclerViewAdapter = BaseAdapter(
                            library,
                            R.layout.library_item_layout,
                            { view ->
                                LibraryViewHolder(view)
                            }, { holder, element ->
                                LibraryViewHolder.callBack(holder = holder, element = element)
                            })

                        recyclerListView.layoutManager = LinearLayoutManager(context)
                        recyclerListView.adapter = recyclerViewAdapter
                    }
                    list.updateUi(library)
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
                            viewModelLibrary.loadLibrary()
                        }
                    }
                    list.updateUi<Library>(null)
                    list.setLoading(false)
                }
            }
        })
        lifecycle.addObserver(viewModelLibrary)
    }
}