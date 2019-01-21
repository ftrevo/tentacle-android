package br.com.concrete.tentacle.repositories

import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SharedPrefRepositoryTest : KoinTest {

    private val sharePrefRepository: SharedPrefRepository by inject()
    private val stringKey = "stringKey"
    private val string = "string"
    private val stringResponseEmpty = ""

    @After
    fun turnOffKoin() {
        stopKoin()
    }

    @Test
    fun `when save a string should be possible to delete it with the same key`() {
        sharePrefRepository.saveString(stringKey, string)
        sharePrefRepository.deleteStoreString(stringKey)
        val stored = sharePrefRepository.getStoreString(stringKey)
        assertEquals(stringResponseEmpty, stored)
    }

    @Test
    fun `when save a string should be possible to retrieve it with the same key`() {
        sharePrefRepository.saveString(stringKey, string)
        val stored = sharePrefRepository.getStoreString(stringKey)
        assertEquals(string, stored)
    }

    @Test
    fun `when save a session should be possible to retrieve it with the same key`() {
        val sessionKey = "sessionKey"
        val sessionForPreference = Session("", "", "")
        sharePrefRepository.saveSession(sessionKey, sessionForPreference)
        val stored = sharePrefRepository.getStoredSession(sessionKey)
        assertEquals(sessionForPreference, stored)
    }

    @Test
    fun `when pass an invalid key to getStoredSession should return null`() {
        val result = sharePrefRepository.getStoredSession("wrongKey")
        assertEquals(null, result)
    }

    @Test
    fun `when pass an invalid key to getStoredString should return ""`() {
        val result = sharePrefRepository.getStoreString("wrongKey")
        assertEquals("", result)
    }

    @Test
    fun `when pass an invalid key to deleteStoreString should return Unit`() {
        val result = sharePrefRepository.deleteStoreString("wrongKey")
        assertEquals(Unit, result)
    }
}