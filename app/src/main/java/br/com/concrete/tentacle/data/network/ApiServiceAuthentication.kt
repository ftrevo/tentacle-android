package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.StateResponse
import br.com.concrete.tentacle.data.models.CityResponse
import br.com.concrete.tentacle.data.models.ForgotPassword
import br.com.concrete.tentacle.data.models.PasswordRecovery
import br.com.concrete.tentacle.data.models.RequestLogin
import br.com.concrete.tentacle.data.models.RequestRefreshToken
import br.com.concrete.tentacle.data.models.User
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceAuthentication {

    @POST("/users")
    fun registerUser(@Body userRequest: UserRequest): Observable<BaseModel<Session>>

    @GET("/states")
    fun getStates(): Observable<BaseModel<StateResponse>>

    @GET("/states/{id}/cities")
    fun getCities(@Path("id") stateId: String): Observable<BaseModel<CityResponse>>

    @POST("login")
    fun loginUser(@Body login: RequestLogin): Flowable<BaseModel<Session>>

    @POST("/users/forgot-password")
    fun forgotPassword(@Body forgetPass: ForgotPassword): Observable<BaseModel<User>>

    @POST("/users/restore-password")
    fun restorePassword(@Body passwordRecovery: PasswordRecovery): Flowable<BaseModel<Session>>

    @POST("/refresh-token")
    fun refreshToken(@Body requestRefreshToken: RequestRefreshToken): Call<BaseModel<Session>>
}