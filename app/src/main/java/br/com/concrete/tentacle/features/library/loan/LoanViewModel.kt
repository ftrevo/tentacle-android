package br.com.concrete.tentacle.features.library.loan

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.models.library.loan.LoanResponse
import br.com.concrete.tentacle.data.repositories.LibraryRepository

class LoanViewModel(private val libraryRepository: LibraryRepository) : BaseViewModel() {
    private val viewStateLibrary: MutableLiveData<ViewStateModel<Library>> = MutableLiveData()
    private val viewStateLoan: MutableLiveData<ViewStateModel<LoanResponse>> = MutableLiveData()

    fun getLibrary() = viewStateLibrary
    fun getLoan() = viewStateLoan

    fun loadLibrary(id: String) {
        viewStateLibrary.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(libraryRepository.getLibrary(id)
            .subscribe({ baseModel ->
                viewStateLibrary.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data))
            }, {
                viewStateLibrary.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }

    fun performLoad(mediaId: String) {
        viewStateLoan.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(libraryRepository.performLoan(mediaId)
            .subscribe({ baseModel ->
                viewStateLoan.postValue(ViewStateModel(status = ViewStateModel.Status.SUCCESS, model = baseModel.data))
            }, {
                viewStateLoan.postValue(ViewStateModel(status = ViewStateModel.Status.ERROR, errors = notKnownError(it)))
            })
        )
    }
}