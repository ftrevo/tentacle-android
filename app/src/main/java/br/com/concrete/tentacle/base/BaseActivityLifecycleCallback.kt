package br.com.concrete.tentacle.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import br.com.concrete.tentacle.data.eventPublisher.EventPublisherContract
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class BaseActivityLifecycleCallback: Application.ActivityLifecycleCallbacks, KoinComponent {

    private val eventPublisher: EventPublisherContract by inject()

    private val disposables = CompositeDisposable()

    override fun onActivityPaused(activity: Activity?) {
        activity?.let {
            if (it is BaseActivity) {
                disposables.clear()
            }
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        activity?.let {
            if (it is BaseActivity) {
                disposables.add(eventPublisher.subscribe(it as Consumer<Any>))
            }
        }
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }

}