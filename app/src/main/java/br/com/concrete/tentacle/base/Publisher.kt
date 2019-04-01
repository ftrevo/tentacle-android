package br.com.concrete.tentacle.base

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

class Publisher {

    private val sourceSubject: BehaviorSubject<Any> = BehaviorSubject.create()

    fun subscribe(contract: () -> Consumer<Any>): Disposable {
        return sourceSubject.subscribe(contract())
    }

    fun publish(value: Any) {
        sourceSubject.onNext(value)
    }

    fun onComplete() {
        sourceSubject.onComplete()
    }
}