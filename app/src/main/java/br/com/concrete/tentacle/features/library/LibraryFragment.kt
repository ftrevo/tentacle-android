package br.com.concrete.tentacle.features.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.ViewStateModel
import org.koin.android.viewmodel.ext.android.viewModel

class LibraryFragment : BaseFragment() {

    private val viewModelLibrary: LibraryViewModel by viewModel()

    override fun getToolbarTitle() = R.string.toolbar_title_library

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {
        initObserver()
    }

    private fun initObserver() {
        viewModelLibrary.getLibrary().observe(this, Observer { stateModel ->
            val library = stateModel.model
            when (stateModel.status) {
                ViewStateModel.Status.SUCCESS -> {
                    library?.let {
                    }
                }
                ViewStateModel.Status.LOADING -> {
                    // TODO
                }

                ViewStateModel.Status.ERROR -> {
                    stateModel.errors?.let {
                        showError(it)
                    }
                }
            }
        })
        lifecycle.addObserver(viewModelLibrary)
    }
}