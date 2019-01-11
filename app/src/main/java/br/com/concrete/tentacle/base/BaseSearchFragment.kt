package br.com.concrete.tentacle.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import br.com.concrete.tentacle.R

abstract class BaseSearchFragment: BaseFragment(),
    SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val menuItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = context!!.getString(R.string.search)

        super.onCreateOptionsMenu(menu, inflater)
    }

    abstract fun init()

}