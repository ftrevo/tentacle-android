package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class LibraryRepository(private val apiRest: ApiService) {

    fun getLibraryList(): Observable<BaseModel<LibraryResponse>> {
        return apiRest.getLibraryList()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun getLibrary(id: String): Observable<BaseModel<Library>> {
        return apiRest.getLibrary(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}