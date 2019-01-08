package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.base.BaseTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.RequestLogin
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.mock.baseModelLoginSuccess
import br.com.concrete.tentacle.mock.error401
import com.google.common.net.MediaType
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import junit.framework.Assert.assertEquals
import okhttp3.ResponseBody
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito
import retrofit2.HttpException
import retrofit2.Response


class LoginRepositoryTest: BaseTest(){

    @InjectMocks
    lateinit var loginRepositoryTest: LoginRepository

    @Test
    fun loginSuccess(){
        Mockito.`when`(apiService.loginUser(RequestLogin("test@test.com", "test")))
            .thenReturn(Flowable.just(baseModelLoginSuccess))

        val testSubscriber = TestSubscriber<BaseModel<Session>>()
        val flowableResult = loginRepositoryTest.loginUser("test@test.com", "test")
        flowableResult.subscribe(testSubscriber)
        testSubscriber.assertComplete()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        val baseModelResult = testSubscriber.values()[0]
        assertEquals(baseModelLoginSuccess, baseModelResult)
    }

    @Test
    fun loginError401(){
        Mockito.`when`(apiService.loginUser(RequestLogin("test@test.com","test")))
            .thenReturn(Flowable.error(
                error401
            ))

        val testSubscriber = TestSubscriber<BaseModel<Session>>()
        loginRepositoryTest.loginUser("test@test.com", "test").subscribe(testSubscriber)
        testSubscriber.assertValueCount(0)
        var er = testSubscriber.errors()[0].cause as HttpException
        assertEquals(401, er.code())
    }
}

