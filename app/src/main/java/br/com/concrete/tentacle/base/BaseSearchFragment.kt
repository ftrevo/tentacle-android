package br.com.concrete.tentacle.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.extensions.callSnackbar
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

const val TIME_OUT: Long = 550
const val MINIMAL_CHARACTER: Int = 2

@SuppressLint("CheckResult")
abstract class BaseSearchFragment : BaseFragment(),
    MenuItem.OnActionExpandListener {

    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initView()
        initViewModel()
        initListener()
        initRecyclerView()
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        setupSearchView(menu, inflater)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupSearchView(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val menuItem: MenuItem = menu.findItem(R.id.action_search)
        when (menuItem.actionView) {
            is SearchView -> initSearchView(menuItem)
        }
    }

    private fun initSearchView(menuItem: MenuItem) {
        searchView = menuItem.actionView as SearchView
        context?.let {
            searchView.queryHint = it.getString(R.string.search)
        }

        searchView.isIconified = false
        searchView.setIconifiedByDefault(true)

        val closeButton = searchView.findViewById<ImageView>(R.id.search_close_btn)
        val editText = searchView.findViewById<EditText>(R.id.search_src_text)

        closeButton.setOnClickListener {
            clearListGame()
            editText.setText("")
            searchView.isIconified = true
            searchView.setIconifiedByDefault(true)
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

    fun validateSearch(search: String) = search.trim().length >= MINIMAL_CHARACTER

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = titleToolbar()
    }

    fun getQuerySearchView() = searchView.query.toString()

    fun callSnackBar(message: String) {
        context?.let { context ->
            view?.let {
                context.callSnackbar(it, message)
            }
        }
    }

    abstract fun titleToolbar(): String
    abstract fun initView()
    abstract fun initListener()
    abstract fun initViewModel()
    abstract fun initRecyclerView()
    abstract fun getSearchGame(searchGame: String)
    abstract fun clearListGame()
}