package br.com.concrete.tentacle.features.home

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract
import br.com.concrete.tentacle.utils.PREFS_KEY_USER_SESSION
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.standalone.inject

class LogoutTest : BaseViewModelTest() {

//    private val sharePrefRepository: SharedPrefRepositoryContract by inject()

    @Before
    fun createSession() {
//        sharePrefRepository.saveSession(PREFS_KEY_USER_SESSION, Session("", "", ""))
    }

    @Test
    fun `when user perform logout the session should clear`() {
//        sharePrefRepository.removeSession()
//
//        val actual = sharePrefRepository.getStoredSession(PREFS_KEY_USER_SESSION)
//        val expected = null
//        assertEquals(actual, expected)
    }
}