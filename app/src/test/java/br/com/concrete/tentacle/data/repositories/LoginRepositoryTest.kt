package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.base.BaseTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.RequestLogin
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.mock.baseModelLoginSuccess
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito

class LoginRepositoryTest: BaseTest(){

    @InjectMocks
    lateinit var loginRepositoryTest: LoginRepository

    @Test
        fun `when call login it returns the session and with the attributes bellow`(){

        Mockito.`when`(apiService.loginUser(RequestLogin("test@test.com", "test")))
            .thenReturn(Flowable.just(baseModelLoginSuccess))
        /** block finishes */

        val testSubscriber = TestSubscriber<BaseModel<Session>>()
        val flowableResult = loginRepositoryTest.loginUser("test@test.com", "test")
        flowableResult.subscribe(testSubscriber)
        assertCompleteNoErrorCount(testSubscriber)
        val baseModelResult = testSubscriber.values()[0]
        assertEquals(baseModelLoginSuccess, baseModelResult)
    }
}

