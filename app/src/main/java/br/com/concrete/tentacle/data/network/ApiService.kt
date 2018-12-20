package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.ResponseModel
import br.com.concrete.tentacle.data.models.Session
import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("user")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Flowable<ResponseModel<Session, Unit>>
}