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
        assertCompleteNoErrorCount(testObserver)
        val baseModelResult = testObserver.values()[0]
        assertEquals(baseModelUserSuccess, baseModelResult)
    }

    @Test
    fun testRepositorySuccessGetStates(){
        Mockito.`when`(apiService.getStates())
            .thenReturn(Observable.just(baseModelStateSuccess))

        val testObserver = TestObserver<BaseModel<StateResponse>>()
        val observerResult = userRepository.getStates()
        observerResult.subscribe(testObserver)
        assertCompleteNoErrorCount(testObserver)
        val baseModelResult = testObserver.values()[0]
        assertEquals(baseModelStateSuccess, baseModelResult)
    }

    @Test
    fun testRepositorySuccessGetCities(){
        Mockito.`when`(apiService.getCities(requestedState))
            .thenReturn(Observable.just(baseModelCitiesSuccess))

        val testObserver = TestObserver<BaseModel<CityResponse>>()
        val observerResult = userRepository.getCities(requestedState)
        observerResult.subscribe(testObserver)
        assertCompleteNoErrorCount(testObserver)
        val baseModelResult = testObserver.values()[0]
        assertEquals(baseModelCitiesSuccess, baseModelResult)
    }
}