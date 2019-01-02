package br.com.concrete.tentacle.data.repositories

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.CityResponse
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.UserRequest
import br.com.concrete.tentacle.data.models.StateResponse

interface UserRepositoryContract {

    fun registerUser(userRequest: UserRequest,
                     success: (BaseModel<User>) -> Unit,
                     error: (Throwable) -> Unit)

    fun getCities(stateId: String,
                  success: (BaseModel<CityResponse>) -> Unit,
                  error: (Throwable) -> Unit)

    fun getStates(success: (BaseModel<StateResponse>) -> Unit,
                  error: (Throwable) -> Unit)

    fun disposeAll()
}