package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.base.BaseTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
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
    fun testRepositorySuccessResponse(){

        val user = User(
            _id = "1",
            name = "daivid",
            email = "daivid@gmail.com",
            phone = "99 123456789",
            password = "123456",
            state = State("hash_code", "PE", "Pernambuco"),
            city = "Recife",
            createdAt = "today",
            updatedAt = "today")
        val message = listOf("User created successfully")
        val baseModel = BaseModel(message, user)

        val userRequest = UserRequest(
            name = "daivid",
            email = "daivid@gmail.com",
            phone = "99 123456789",
            password = "123456",
            state = "hash_code",
            city = "Recife"
        )

        Mockito.`when`(apiService.registerUser(userRequest))
            .thenReturn(Observable.just(baseModel))

        val testObserver = TestObserver<BaseModel<User>>()
        val observerResult = userRepository.registerUser(userRequest)
        observerResult.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val baseModelResult = testObserver.values()[0]
        assertEquals(baseModel, baseModelResult)
    }

}