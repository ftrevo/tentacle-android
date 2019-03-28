package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.User

interface SharedPrefRepositoryContract {

    fun saveString(key: String, value: String)

    fun saveSession(key: String, session: Session)

    fun removeSession()

    fun removeUser()

    fun getStoredSession(key: String): Session?

    fun getStoredUser(key: String): User?

    fun updateUser(user: User)

    fun saveUser(key: String, user: User)

    fun getStoreString(key: String): String?

    fun deleteStoreString(key: String)
}
