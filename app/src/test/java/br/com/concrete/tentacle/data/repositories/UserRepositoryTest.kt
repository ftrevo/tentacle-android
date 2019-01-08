package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.base.BaseTest
import br.com.concrete.tentacle.data.models.*
import br.com.concrete.tentacle.mock.*
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito
import retrofit2.HttpException


class UserRepositoryTest: BaseTest(){

    @InjectMocks
    private lateinit var userRepository: UserRepository

    @Test
    fun testRepositorySuccessRegister(){
        Mockito.`when`(apiService.registerUser(userRequest))
            .thenReturn(Observable.just(baseModelUserSuccess))

        val testObserver = TestObserver<BaseModel<User>>()
        val observerResult = userRepository.registerUser(userRequest)
        observerResult.subscribe(testObserver)
        assertSuccess(testObserver, baseModelUserSuccess)
    }

    @Test
    fun testRepositorySuccessGetStates(){
        Mockito.`when`(apiService.getStates())
            .thenReturn(Observable.just(baseModelStateSuccess))

        val testObserver = TestObserver<BaseModel<StateResponse>>()
        val observerResult = userRepository.getStates()
        observerResult.subscribe(testObserver)
        assertSuccess(testObserver, baseModelStateSuccess)
    }

    @Test
    fun testRepositorySuccessGetCities(){
        Mockito.`when`(apiService.getCities(requestedState))
            .thenReturn(Observable.just(baseModelCitiesSuccess))

        val testObserver = TestObserver<BaseModel<CityResponse>>()
        val observerResult = userRepository.getCities(requestedState)
        observerResult.subscribe(testObserver)
        assertSuccess(testObserver, baseModelCitiesSuccess)
    }

    @Test
    fun testRepositoryRegisterError(){
        Mockito.`when`(apiService.registerUser(userRequest))
            .thenReturn(Observable.error(
                errorWithMessage
            ))

        val testObserver = TestObserver<BaseModel<User>>()
        val observerResult = userRepository.registerUser(userRequest)
        observerResult.subscribe(testObserver)
        assertError400(testObserver)
    }

    @Test
    fun testRepositoryGetStatesError(){
        Mockito.`when`(apiService.getStates())
            .thenReturn(Observable.error(errorWithMessage))

        val testObserver = TestObserver<BaseModel<StateResponse>>()
        val observerResult = userRepository.getStates()
        observerResult.subscribe(testObserver)
        assertError400(testObserver)
    }

    @Test
    fun testRepositoryGetCitiesError(){
        Mockito.`when`(apiService.getCities(requestedState))
            .thenReturn(Observable.error(errorWithMessage))

        val testObserver = TestObserver<BaseModel<CityResponse>>()
        val observerResult = userRepository.getCities(requestedState)
        observerResult.subscribe(testObserver)
        assertError400(testObserver)
    }

    private fun <T> assertSuccess(testObserver: TestObserver<*>, expected: T){
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        assertEquals(expected, testObserver.values()[0] as T)
    }

    private fun assertError400(testObserver: TestObserver<*>){
        testObserver.assertNotComplete()
        testObserver.assertValueCount(0)
        var er = testObserver.errors()[0].cause as HttpException
        assertEquals(400, er.code())
    }
}