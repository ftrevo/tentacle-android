package br.com.concrete.tentacle.features.user

import br.com.concrete.tentacle.data.models.*

interface UserViewModelContract {

    fun callBackUser(user: BaseModel<User>)

    fun callBackStates(states: BaseModel<ResponseState>)

    fun callBackCities(cities: BaseModel<ResponseCity>)

    fun notKnownError(error: Throwable)


}