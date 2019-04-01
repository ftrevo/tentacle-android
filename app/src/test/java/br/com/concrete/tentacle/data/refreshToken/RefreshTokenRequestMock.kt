package br.com.concrete.tentacle.data.refreshToken

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.User
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.UserRepository
import com.google.gson.GsonBuilder
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException

class RefreshTokenRequestMock(private val userRepository: UserRepository) {

    private val disposables = CompositeDisposable()
    private val gson = GsonBuilder().create()

    private val viewStateProfile: MutableLiveData<ViewStateModel<User>> = MutableLiveData()
    fun getProfileViewState() = viewStateProfile

    fun executeRequest() {
        disposables.add(
            userRepository.getProfile().subscribe({
                viewStateProfile.postValue(ViewStateModel(
                    status = ViewStateModel.Status.SUCCESS,
                    model = it.data))
            }, {
                if (it is HttpException) {
                    val errorResponse = gson
                        .fromJson(it.response().errorBody()?.charStream(),
                            ErrorResponse::class.java)
                    errorResponse.statusCode = it.code()

                    viewStateProfile.postValue(
                        ViewStateModel(
                            status = ViewStateModel.Status.ERROR,
                            errors = errorResponse)
                    )
                }
            })
        )
    }

}