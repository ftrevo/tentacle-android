package br.com.concrete.tentacle.viewmodel

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.register.RegisterUserViewModel
import br.com.concrete.tentacle.mock.baseModelCitiesSuccess
import br.com.concrete.tentacle.mock.baseModelStateSuccess
import br.com.concrete.tentacle.mock.requestedState
import br.com.concrete.tentacle.mock.userRequest
import br.com.concrete.tentacle.mock.baseModelUserSuccess
import br.com.concrete.tentacle.mock.error400
import br.com.concrete.tentacle.mock.errorResponse
import br.com.concrete.tentacle.repositories.UserRepository
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class RegisterUserMVTest : BaseViewModelTest() {

    @Mock
    lateinit var userRepository: UserRepository

    private lateinit var userViewModelTest: RegisterUserViewModel

    @Before
    fun before() {
        userViewModelTest = RegisterUserViewModel(userRepository)
    }

    @Test
    fun testLoadCities() {
        val expected =
            ViewStateModel(status = ViewStateModel.Status.SUCCESS,
                model = baseModelCitiesSuccess.data.cities)
        var actual = ViewStateModel<ArrayList<String>>(status = ViewStateModel.Status.LOADING)

        Mockito.`when`(userRepository.getCities(requestedState))
            .thenReturn(Observable.just(baseModelCitiesSuccess))

        val result = userViewModelTest.getCities()
        result.observeForever {
            actual = it
        }

        userViewModelTest.loadCities(requestedState)
        assertEquals(expected, actual)
    }

    @Test
    fun testLoadStates() {
        val expected =
            ViewStateModel(status = ViewStateModel.Status.SUCCESS,
                model = baseModelStateSuccess.data.list)
        var actual = ViewStateModel<ArrayList<State>>(status = ViewStateModel.Status.LOADING)

        Mockito.`when`(userRepository.getStates())
            .thenReturn(Observable.just(baseModelStateSuccess))

        val result = userViewModelTest.getStates()
        result.observeForever {
            actual = it
        }

        userViewModelTest.loadStates()
        assertEquals(expected, actual)
    }

    @Test
    fun testGetRegisterUser() {
        val expected = ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModelUserSuccess.data)
        var actual = ViewStateModel<User>(status = ViewStateModel.Status.LOADING)
        Mockito.`when`(userRepository.registerUser(userRequest))
            .thenReturn(Observable.just(baseModelUserSuccess))

        val result = userViewModelTest.getUser()
        result.observeForever {
            actual = it
        }

        userViewModelTest.registerUser(baseModelUserSuccess.data)
        assertEquals(expected, actual)
    }

    @Test
    fun loadCitiesError() {
        val expected =
            ViewStateModel<ArrayList<String>>(status = ViewStateModel.Status.ERROR,
                errors = errorResponse)
        var actual = ViewStateModel<ArrayList<String>>(status = ViewStateModel.Status.LOADING)

        Mockito.`when`(userRepository.getCities(requestedState))
            .thenReturn(Observable.error(error400))

        val result = userViewModelTest.getCities()
        result.observeForever {
            actual = it
        }

        userViewModelTest.loadCities(requestedState)
        assertEquals(expected.status, actual.status)
    }

    @Test
    fun loadStatesError() {
        val expected =
            ViewStateModel<ArrayList<State>>(status = ViewStateModel.Status.ERROR,
                errors = errorResponse)
        var actual = ViewStateModel<ArrayList<State>>(status = ViewStateModel.Status.LOADING)

        Mockito.`when`(userRepository.getStates())
            .thenReturn(Observable.error(error400))

        val result = userViewModelTest.getStates()
        result.observeForever {
            actual = it
        }

        userViewModelTest.loadStates()
        assertEquals(expected.status, actual.status)
    }

    @Test
    fun getRegisterUserError() {
        val expected = ViewStateModel<User>(status = ViewStateModel.Status.ERROR, errors = errorResponse)
        var actual = ViewStateModel<User>(status = ViewStateModel.Status.LOADING)
        Mockito.`when`(userRepository.registerUser(userRequest))
            .thenReturn(Observable.error(error400))

        val result = userViewModelTest.getUser()
        result.observeForever {
            actual = it
        }

        userViewModelTest.registerUser(baseModelUserSuccess.data)
        assertEquals(expected.status, actual.status)
    }
}