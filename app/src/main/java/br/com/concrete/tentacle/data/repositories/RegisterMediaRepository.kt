package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.MediaRequest
import br.com.concrete.tentacle.data.network.ApiService
import br.com.concrete.tentacle.testing.OpenForTesting
import io.reactivex.schedulers.Schedulers

@OpenForTesting
class RegisterMediaRepository(private val apiService: ApiService) {

    fun registerMedia(media: MediaRequest) = apiService.registerMedia(media)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io())
}