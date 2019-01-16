package br.com.concrete.tentacle.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import br.com.concrete.tentacle.R


abstract class BaseSearchFragment: BaseFragment(),
    SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
        initListener()
        initRecyclerView()
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val menuItem: MenuItem = menu.findItem(R.id.action_search)
        searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = context!!.getString(R.string.search)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).supportActionBar?.title = titleToolbar()
    }

    fun getQuerySearchView() = searchView.query.toString()

    abstract fun titleToolbar(): String
    abstract fun initListener()
    abstract fun initViewModel()
    abstract fun initRecyclerView()

}