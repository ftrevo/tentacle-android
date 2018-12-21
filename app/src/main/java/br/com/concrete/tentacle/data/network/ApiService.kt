package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.RequestLogin
import br.com.concrete.tentacle.data.models.ResponseModel
import br.com.concrete.tentacle.data.models.Session
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun loginUser(@Body login: RequestLogin): Flowable<ResponseModel<Session, Unit>>
}