package br.com.concrete.tentacle.base

import androidx.fragment.app.Fragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.RunWith
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

@Ignore
@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentedTest : KoinComponent {

    private val sharedPrefRepositoryContract: SharedPrefRepositoryContract by inject()

    lateinit var mockWebServer: MockWebServer
    lateinit var testFragment: Fragment

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        sharedPrefRepositoryContract.removeSession()
        sharedPrefRepositoryContract.removeUser()
    }

    @After
    fun after() {
        mockWebServer.shutdown()
        sharedPrefRepositoryContract.removeSession()
        sharedPrefRepositoryContract.removeUser()
    }
}