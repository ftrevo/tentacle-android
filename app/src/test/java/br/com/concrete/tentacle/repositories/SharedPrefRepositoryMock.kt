package br.com.concrete.tentacle.repositories

import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.repositories.SharedPrefRepositoryContract

class SharedPrefRepositoryMock : SharedPrefRepositoryContract {
    override fun saveString(key: String, value: String) {
    }

    override fun saveSession(session: Session) {
    }

    override fun removeSession() {
    }

    override fun removeUser() {
    }

    override fun getStoredSession(key: String): Session? {
        return Session("", "", "")
    }

    override fun getStoredUser(key: String): User? {
        return User(_id = "1", email = "mock@mock.com", phone = "123456", city = "Recife", state = State("1", "PE", "Pernambuco"), password = "", name = "")
    }

    override fun updateUser(user: User) {
    }

    override fun saveUser(key: String, user: User) {
    }

    override fun getStoreString(key: String): String? {
        return ""
    }

    override fun deleteStoreString(key: String) {
    }
}