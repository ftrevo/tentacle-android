package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.*
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("/games")
    fun getSearchGames(
        @Query("title")
        title: String
    ): Observable<BaseModel<GameResponse>>

    @POST("/games")
    fun registerNewGame(
        @Body
        game: GameRequest
    ): Observable<BaseModel<Game>>

    @GET("/media")
    fun getRegisteredGames(@Query("mineOnly") mineOnly: Boolean = true): Observable<BaseModel<MediaResponse>>

    @POST("/media")
    fun registerMedia(@Body media: MediaRequest): Observable<BaseModel<Media>>

    @GET("/games")
    fun loadHomeGames(): Observable<BaseModel<GameResponse>>

    @GET("library")
    fun getLibrary(): Observable<BaseModel<LibraryResponse>>
}