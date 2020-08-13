package br.com.concrete.tentacle.data.eventPublisher

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

interface EventPublisherContract {
    fun subscribe(contract: Consumer<Any>): Disposable
    fun publish(value: Any)
    fun onComplete()
}