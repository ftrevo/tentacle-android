package br.com.concrete.tentacle.utils

import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.library.filter.SubItem

object QueryUtils {

    fun filterQuery(filters: ArrayList<SubItem>): QueryParameters {
        val queries = QueryParameters()
        filters.forEach { subItem ->
            when (subItem.key) {
                "Plataformas" -> {
                    queries.mediaPlatform?.add(subItem.keyValue) ?: run {
                        queries.mediaPlatform = ArrayList<String>()
                        queries.mediaPlatform!!.add(subItem.keyValue)
                    }
                }
                "Status do jogo" -> {
                    queries.status = subItem.keyValue
                }
            }
        }

        return queries
    }
}