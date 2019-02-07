package br.com.concrete.tentacle.features.library.filter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.filter.FilterItem
import br.com.concrete.tentacle.data.repositories.FilterRepository

class FilterViewModel(private val filterRepository: FilterRepository) : BaseViewModel() {

    val viewStateFilters: MutableLiveData<ViewStateModel<List<FilterItem>>> = MutableLiveData()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun getFilterItems() {
        disposables.add(
            filterRepository.getFilterItems()
                .subscribe(
                    {
                        viewStateFilters
                            .postValue(ViewStateModel(
                                status = ViewStateModel.Status.SUCCESS,
                                model = it))
                    },
                    {
                        viewStateFilters
                            .postValue(ViewStateModel(
                                status = ViewStateModel.Status.ERROR,
                                errors = notKnownError(it))
                            )
                    }
                )

        )
    }
}