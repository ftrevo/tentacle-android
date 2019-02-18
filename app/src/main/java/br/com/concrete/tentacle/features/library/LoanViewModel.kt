package br.com.concrete.tentacle.features.library

import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.data.repositories.LibraryRepository

class LoanViewModel(private val libraryRepository: LibraryRepository) : BaseViewModel() {
    private val viewStateLibrary: MutableLiveData<ViewStateModel<Library>> = MutableLiveData()

    fun getLibrary() = viewStateLibrary

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
}