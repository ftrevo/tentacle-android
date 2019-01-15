package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.*
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Header

interface ApiService {

    @POST("/users")
    fun registerUser(@Body userRequest: UserRequest): Observable<BaseModel<User>>

    @GET("/states")
    fun getStates(): Observable<BaseModel<StateResponse>>

    @GET("/states/{id}/cities")
    fun getCities(@Path("id") stateId: String): Observable<BaseModel<CityResponse>>

    @POST("login")
    fun loginUser(@Body login: RequestLogin): Flowable<BaseModel<Session>>

}