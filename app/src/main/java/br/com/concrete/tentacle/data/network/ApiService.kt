package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaRequest
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.models.library.loan.LoanRequest
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/games")
    fun getSearchGames(
        @Query("name")
        name: String
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
        @Query("name") search: String? = null,
        @Query("mediaPlatform") mediaPlatform: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null
    ): Observable<BaseModel<LibraryResponse>>

    fun getLibraryList(): Observable<BaseModel<LibraryResponse>>

    @GET("library/{id}")
    fun getLibrary(@Path("id") id: String): Observable<BaseModel<Library>>

    @POST("loans")
    fun performLoan(@Body loanRequest: LoanRequest): Observable<BaseModel<LoanResponse>>
}