package br.com.concrete.tentacle.data.models

import androidx.lifecycle.LiveData

class StatusLiveData : LiveData<ViewStateModel<Unit>>() {

    public override fun postValue(value: ViewStateModel<Unit>? ) {
        super.postValue(value)
    }
}