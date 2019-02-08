package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.network.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class LibraryRepository(private val apiRest: ApiService) {

    fun getLibrary(): Observable<BaseModel<LibraryResponse>> {
        return apiRest.getLibrary()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}