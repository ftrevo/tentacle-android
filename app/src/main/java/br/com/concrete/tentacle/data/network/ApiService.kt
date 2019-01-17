package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.*
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService{

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
    fun getRegisteredGames(
        @Query("mineOnly")
        mineOnly: Boolean
    ): Observable<BaseModel<MediaResponse>>

}