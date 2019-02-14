package br.com.concrete.tentacle.utils

import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.library.filter.SubItem

object QueryUtils {

    fun assemblefilterQuery(filters: ArrayList<SubItem>): QueryParameters {
        val queries = QueryParameters()
        filters.forEach { subItem ->
            when (subItem.queryType) {
                "Plataformas" -> {
                    queries.mediaPlatform?.add(subItem.queryParameter) ?: run {
                        queries.mediaPlatform = ArrayList<String>()
                        queries.mediaPlatform!!.add(subItem.queryParameter)
                    }
                }
                "Status do jogo" -> {
                    queries.status = subItem.queryParameter
                }
            }
        }

        return queries
    }
}