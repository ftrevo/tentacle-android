package br.com.concrete.tentacle.data.eventPublisher

import br.com.concrete.tentacle.utils.SingleEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

class EventPublisher : EventPublisherContract {

    private val sourceSubject: BehaviorSubject<Any> = BehaviorSubject.create()

    override fun subscribe(contract: Consumer<Any>): Disposable {
        return sourceSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(contract)
    }

    override fun publish(value: Any) {
        sourceSubject.onNext(SingleEvent(value))
    }

    override fun onComplete() {
        sourceSubject.onComplete()
    }
}