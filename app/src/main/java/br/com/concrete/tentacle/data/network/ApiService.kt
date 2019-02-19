package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaRequest
import br.com.concrete.tentacle.data.models.MediaResponse
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
    fun getLibrary(
        @Query("_id") id: String? = null,
        @Query("title") title: String? = null,
        @Query("mediaOwner") mediaOwner: String? = null,
        @Query("mediaId") mediaId: String? = null,
        @Query("mediaPlatform") mediaPlatform: List<String>? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("title") search: String? = null
    ): Observable<BaseModel<LibraryResponse>>
}