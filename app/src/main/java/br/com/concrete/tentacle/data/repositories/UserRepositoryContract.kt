package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.features.user.UserViewModelContract

interface UserRepositoryContract {


    fun registerUser(user: User, callBack: UserViewModelContract)

    fun getCities(stateId: String, callBack: UserViewModelContract)

    fun getStates(callBack: UserViewModelContract)

}