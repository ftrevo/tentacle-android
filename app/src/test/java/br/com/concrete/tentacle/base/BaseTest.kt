package br.com.concrete.tentacle.base

import android.app.Instrumentation
import br.com.concrete.tentacle.data.network.ApiService
import br.com.concrete.tentacle.di.networkModule
import br.com.concrete.tentacle.di.repositoryModule
import br.com.concrete.tentacle.di.viewModelModule
import br.com.concrete.tentacle.rules.RxImmediateSchedulerRule
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@Ignore
@RunWith(MockitoJUnitRunner::class)
open class BaseTest: Instrumentation(), KoinTest {

    @Mock
    lateinit var apiService: ApiService

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Before
    fun before() {
        StandAloneContext.startKoin(
            listOf(
                networkModule,
                viewModelModule,
                repositoryModule
            ))
    }

    @After
    fun after() {
        StandAloneContext.stopKoin()
    }

    fun <T> assertCompleteNoErrorCount(testObserver: TestObserver<T>){
        verifyCompleteNoErrorCount(testObserver)
    }

    fun <T> assertCompleteNoErrorCount(testSubscriber: TestSubscriber<T>){
        verifyCompleteNoErrorCount(testSubscriber)
    }

    private fun <T> verifyCompleteNoErrorCount(t: T){
        if( t is TestSubscriber<*> || t is TestSubscriber<*>){
                t.assertComplete()
                t.assertNoErrors()
                t.assertValueCount(1)
        }
    }
}