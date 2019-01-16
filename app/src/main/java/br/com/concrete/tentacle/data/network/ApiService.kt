package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.*
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    @POST("/users")
    fun registerUser(@Body userRequest: UserRequest): Observable<BaseModel<User>>

    @GET("/states")
    fun getStates(): Observable<BaseModel<StateResponse>>

    @GET("/states/{id}/cities")
    fun getCities(@Path("id") stateId: String): Observable<BaseModel<CityResponse>>

    @POST("login")
    fun loginUser(@Body login: RequestLogin): Flowable<BaseModel<Session>>

    @GET("/media")
    fun getRegisteredGames(@Query("mineOnly") mineOnly: Boolean,
                           @Header("Authorization")
                           authKey: String): Observable<BaseModel<MediaResponse>>

    @GET("/games")
    fun getSearchGames(
        @Query("title")
        title: String,
        @Header("Authorization")
        authKey: String
    ): Observable<BaseModel<GameResponse>>

    @POST("/games")
    fun registerNewGame(
        @Body
        game: GameRequest,
        @Header("Authorization")
        authKey: String
    ): Observable<BaseModel<Game>>
}