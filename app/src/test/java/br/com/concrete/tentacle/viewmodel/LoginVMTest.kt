package br.com.concrete.tentacle.viewmodel

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.login.LoginViewModel
import com.google.gson.GsonBuilder
import io.reactivex.subscribers.TestSubscriber
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.standalone.inject

class LoginVMTest: BaseViewModelTest(){

    private lateinit var loginViewMock: LoginViewModel

    @Test
    fun testLogin() {
        val loginViewMock: LoginViewModel by inject()

        val testS = TestSubscriber<BaseModel<Session>>()
        val responseJson = this::class.java.classLoader?.getResource("mockjson/login/login_success.json")?.readText()

        val responseObject: BaseModel<*> = GsonBuilder().create().fromJson(responseJson, BaseModel::class.java)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject)
        var actual = ViewStateModel<Session>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        loginViewMock.getStateModel().observeForever {
            actual = it
        }
        loginViewMock.loginUser("daivid.v.leal@concrete.com.br", "123456")

        assertEquals(expected, actual)

    }


//
//    @Test
//    fun test() {
//        // Create a MockWebServer. These are lean enough that you can create a new
//        // instance for every unit test.
//        val server = MockWebServer()
//
//        // Schedule some responses.
//        server.enqueue(MockResponse().setBody("hello, world!"))
//        server.enqueue(MockResponse().setBody("sup, bra?"))
//        server.enqueue(MockResponse().setBody("yo dog"))
//
//        // Start the server.
//        server.start()


     // Ask the server for its URL. You'll need this to make HTTP requests.

//        loginRepository.loginUser("daivid.v.leal@concrete.com.br", "123456")
//        loginRepository.loginUser("daivid.v.leal@concrete.com.br", "123456")
//        loginRepository.loginUser("daivid.v.leal@concrete.com.br", "123456")

     // Exercise your application code, which should make those HTTP requests.
      // Responses are returned in the same order that they are enqueued.

     // Optional: confirm that your app made the HTTP requests you were expecting.
//      val request1 = server.takeRequest()
//    assertEquals("/v1/chat/messages/", request1.path)
//    assertNotNull(request1.getHeader("Authorization"))
//
//    val request2 = server.takeRequest()
//    assertEquals("/v1/chat/messages/2", request2.path)
//
//    val request3 = server.takeRequest()
//    assertEquals("/v1/chat/messages/3", request3.path)
//
//     // Shut down the server. Instances cannot be reused.
//      server.shutdown()
//    }

//    @Before @Throws fun setUp() {
//        // Initialize mock webserver
//        mockServer = MockWebServer()
//        // Start the local server
//        mockServer.start()
//
//        // Get an okhttp client
//        val okHttpClient = OkHttpClient.Builder()
//            .build()
//
//        // Get an instance of Retrofit
//        val retrofit = Retrofit.Builder()
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .baseUrl(mockServer.url("/"))
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .build()
//
//        // Get an instance of blogService
//        blogService = retrofit.create(ApiServiceAuthentication::class.java)
//        // Initialized repository
//        blogRepository = LoginRepository(blogService)
//    }
//
//    @After @Throws fun tearDown() {
//        // We're done with tests, shut it down
//        mockServer.shutdown()
//    }


    @Test
    fun testLoginErrorMockito() {

    }

    @Test
    fun testLoginErrorMockito401() {

    }
}

//        val api = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create(Gson()))
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .baseUrl(mockServer.url("/"))
//            .client(
//                OkHttpClient.Builder()
//                    .connectTimeout(30L, TimeUnit.SECONDS)
//                    .readTimeout(15L, TimeUnit.SECONDS)
//                    .build()
//            )
//            .build().create(ApiServiceAuthentication::class.java)
//
//        api.loginUser(RequestLogin("daivid.v.leal@concrete.com.br", "123456"))
//            .subscribe(testS)
//
//        testS.assertComplete()
//        testS.assertValueCount(1)
//        val result = testS.values()[0]
//
//        assertEquals(expected.model, result)
