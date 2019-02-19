package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class LibraryRepository(private val apiRest: ApiService) {

    fun getLibrary(queries: QueryParameters, search: String?): Observable<BaseModel<LibraryResponse>> {
        return apiRest.getLibrary(
            queries.id,
            queries.title,
            queries.mediaOwner,
            queries.mediaId,
            queries.mediaPlatform,
            queries.limit,
            queries.page,
            search
        )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}