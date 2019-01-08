package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.features.MainActivity
import br.com.concrete.tentacle.mock.*
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SharedPrefRepositoryTest: KoinTest{

    private lateinit var activity: MainActivity

    private val sharePrefRepository: SharedPrefRepository by inject()

    @Before
    fun createContext(){
        activity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .get()
    }

    @After
    fun destroyContext(){
        StandAloneContext.stopKoin()
    }

    @Test
    fun testRemove(){
        sharePrefRepository.saveString(stringKey, string)
        sharePrefRepository.deleteStoreString(stringKey)
        val stored = sharePrefRepository.getStoreString(stringKey)
        assertEquals(stringExpectedWhenThereIsNoOne, stored)
    }

    @Test
    fun testSaveAndRetrieveString(){
        sharePrefRepository.saveString(stringKey, string)
        val stored = sharePrefRepository.getStoreString(stringKey)
        assertEquals(string, stored)
    }


    @Test
    fun testSaveAndRetrieveSession(){
        sharePrefRepository.saveSession(sessionKey, sessionForPreference)
        val stored = sharePrefRepository.getStoredSession(sessionKey)
        assertEquals(sessionForPreference, stored)
    }

    @Test
    fun testInvalidKey(){
        sharePrefRepository.getStoredSession("wrongKey")
        sharePrefRepository.getStoreString("wrongKey")
        sharePrefRepository.deleteStoreString("wrongKey")
    }
}