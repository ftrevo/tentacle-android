package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.GameResponse
import br.com.concrete.tentacle.data.models.LoanActionRequest
import br.com.concrete.tentacle.data.models.LoansListResponse
import br.com.concrete.tentacle.data.models.Media
import br.com.concrete.tentacle.data.models.MediaRequest
import br.com.concrete.tentacle.data.models.MediaResponse
import br.com.concrete.tentacle.data.models.MessageReturn
import br.com.concrete.tentacle.data.models.RememberDeliveryResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.models.RequestUpdateToken
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.LibraryResponse
import br.com.concrete.tentacle.data.models.library.loan.LoanRequest
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.utils.LIMIT_PAGE
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/games")
    fun getSearchGames(
        @Query("name") name: String,
        @Query("limit") limit: Int = LIMIT_PAGE,
        @Query("page") page: Int = 0
    ): Observable<BaseModel<GameResponse>>

    @POST("/device-token")
    fun sendToken(
        @Body
        deviceToken: RequestUpdateToken
    ): Observable<BaseModel<MessageReturn>>

    @POST("/games")
    fun registerNewGame(
        @Body
        game: GameRequest
    ): Observable<BaseModel<Game>>

    @DELETE("/media/{id}")
    fun deleteMedia(
        @Path("id") mediaId: String
    ): Observable<BaseModel<Media>>

    @GET("/media-loan")
    fun getRegisteredGames(
        @Query("limit")
        limit: Int = LIMIT_PAGE,
        @Query("page")
        page: Int,
        @Query("active") active: Boolean = true
    ): Observable<BaseModel<MediaResponse>>

    @GET("/loans")
    fun getMyLoans(
        @Query("mineOnly")
        mineOnly: Boolean = true,
        @Query("limit")
        limit: Int = LIMIT_PAGE,
        @Query("page")
        page: Int
    ): Observable<BaseModel<LoansListResponse>>

    @GET("/loans/{loanId}")
    fun getMyLoan(@Path("loanId") loanId: String): Observable<BaseModel<LoanResponse>>

    @POST("/media")
    fun registerMedia(@Body media: MediaRequest): Observable<BaseModel<Media>>

    @GET("/games")
    fun loadHomeGames(): Observable<BaseModel<GameResponse>>

    @GET("games/remote")
    fun loadRemoteGames(
        @Query("name") gameName: String,
        @Query("limit") limit: Int = 50,
        @Query("page") page: Int = 0
    ): Observable<BaseModel<GameResponse>>

    @POST("games/remote")
    fun registerRemoteGame(@Body game: GameRequest): Observable<BaseModel<Game>>

    @GET("library")
    fun getLibrary(
        @Query("_id") id: String? = null,
        @Query("name") search: String? = null,
        @Query("mediaPlatform") mediaPlatform: String? = null,
        @Query("limit") limit: Int? = 99,
        @Query("page") page: Int? = 0
    ): Observable<BaseModel<LibraryResponse>>

    fun getLibraryList(): Observable<BaseModel<LibraryResponse>>

    @GET("library/{id}")
    fun getLibrary(@Path("id") id: String): Observable<BaseModel<Library>>

    @POST("loans")
    fun performLoan(@Body loanRequest: LoanRequest): Observable<BaseModel<LoanResponse>>

    @GET("media-loan/{id}")
    fun getMediaLoan(@Path("id") id: String, @Query("active") active: Boolean = true): Observable<BaseModel<Media>>

    @PATCH("loans/{id}")
    fun updateMediaLoan(
        @Path("id") activeLoanId: String,
        @Body loanAction: LoanActionRequest
    ): Observable<BaseModel<LoanResponse>>

    @GET("games/{id}")
    fun getDetailsGame(@Path("id") idGame: String): Observable<BaseModel<Game>>

    @GET("/library/home")
    fun loadHome(): Observable<BaseModel<GameResponse>>

    @GET("users/profile")
    fun getProfile(): Observable<BaseModel<User>>

    @PATCH("users/{id}")
    fun updateUserProfile(@Path("id") userId: String, @Body user: UserRequest): Observable<BaseModel<Session>>

    @POST("loans/{id}/remember-delivery")
    fun rememberDelivery(@Path("id")id: String?): Observable<BaseModel<RememberDeliveryResponse>>
}