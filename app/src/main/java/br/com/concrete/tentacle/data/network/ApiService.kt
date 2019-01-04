package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.StateResponse
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.models.CityResponse
import br.com.concrete.tentacle.data.models.RequestLogin
import br.com.concrete.tentacle.data.models.Session
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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