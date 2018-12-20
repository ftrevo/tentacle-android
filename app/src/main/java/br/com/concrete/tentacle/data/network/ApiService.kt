package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.*
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    @POST("/users")
    fun registerUser(@Body user: User) : Observable<BaseModel<User>>

    @GET("/states")
    fun getStates() : Observable<BaseModel<ResponseState>>

    @GET("/states/{id}/cities")
    fun getCities(@Path("id") id: String) : Observable<BaseModel<ResponseCity>>

}