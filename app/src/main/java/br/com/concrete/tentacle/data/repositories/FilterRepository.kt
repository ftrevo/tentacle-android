package br.com.concrete.tentacle.data.repositories

import android.content.res.Resources
import br.com.concrete.tentacle.data.models.library.filter.FilterItem
import br.com.concrete.tentacle.extensions.fromJson
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class FilterRepository(private val resources: Resources) {

    fun getFilterItems(): Observable<List<FilterItem>> {
        val filterJson = getJson("filter_itens.json")
        val filterItems = Gson().fromJson<List<FilterItem>>(filterJson)
        return Observable.just(filterItems)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    private fun getJson(path: String): String =
        resources
            .assets
            .open(path).bufferedReader().use { it.readText() }
}