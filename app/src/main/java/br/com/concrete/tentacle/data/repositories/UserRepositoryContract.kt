package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.State
import br.com.concrete.tentacle.data.models.User

interface UserRepositoryContract {


    fun registerUser(user: User)

    fun getCities(state_id: String)

    fun getStates()

    interface Response{
        fun callBackStates(states: List<State>)

        fun onError(errorId: List<String>)
    }

}