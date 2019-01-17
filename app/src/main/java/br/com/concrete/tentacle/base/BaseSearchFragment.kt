package br.com.concrete.tentacle.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import br.com.concrete.tentacle.R
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

const val TIME_OUT: Long = 300
const val MINIMAL_CHARACTER: Int = 3

@SuppressLint("CheckResult")
abstract class BaseSearchFragment : BaseFragment(),
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
        searchView.queryHint = context!!.getString(R.string.search)

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
            .distinct()
            .filter { text -> text.trim().length > MINIMAL_CHARACTER }
            .subscribe { text ->
                getSearchGame(text)
            }

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
    abstract fun getSearchGame(searchGame: String)

}