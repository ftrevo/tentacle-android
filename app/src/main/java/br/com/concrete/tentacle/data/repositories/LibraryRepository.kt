package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.QueryParameters
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.models.library.loan.LoanRequest
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class LibraryRepository(private val apiRest: ApiService) {

    fun getLibrary(queries: QueryParameters, search: String?): Observable<BaseModel<LibraryResponse>> {
        return apiRest.getLibrary(
            queries.id,
            search,
            if(queries.toString() == "") null else queries.toString(),
            queries.limit,
            queries.page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun getLibrary(id: String): Observable<BaseModel<Library>> {
        return apiRest.getLibrary(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun performLoan(mediaId: String): Observable<BaseModel<LoanResponse>> {
        return apiRest.performLoan(LoanRequest(mediaId))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}