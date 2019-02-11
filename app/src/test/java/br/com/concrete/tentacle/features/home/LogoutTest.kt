package br.com.concrete.tentacle.features.home

import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.repositories.SharedPrefRepository
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LogoutTest : KoinTest{

    private val sharePrefRepository: SharedPrefRepository by inject()

    @Before
    fun createSession(){
        sharePrefRepository.saveSession(PREFS_KEY_USER_SESSION, Session("qualquer token", "qualquer refresh token", "qualquer token type"))
    }

    @Test
    fun `when user perform logout the session should clear`() {
        sharePrefRepository.removeSession()

        val actual = sharePrefRepository.getStoredSession(PREFS_KEY_USER_SESSION)
        val expected = null
        assertEquals(actual, expected)

    }


}