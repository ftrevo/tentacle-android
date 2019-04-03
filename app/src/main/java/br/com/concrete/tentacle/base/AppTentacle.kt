package br.com.concrete.tentacle.base

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import br.com.concrete.tentacle.BuildConfig
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.di.PROPERTY_BASE_URL
import br.com.concrete.tentacle.di.androidModule
import br.com.concrete.tentacle.di.eventsModule
import br.com.concrete.tentacle.di.networkModule
import br.com.concrete.tentacle.di.repositoryModule
import br.com.concrete.tentacle.di.viewModelModule
import br.com.concrete.tentacle.utils.LogWrapper
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin

class AppTentacle : Application() {

    companion object {
        var TOKEN: String = String()
    }

    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Answers(), Crashlytics())

        startKoin(this,
            listOf(androidModule,
                networkModule,
                viewModelModule,
                repositoryModule,
                eventsModule),

            extraProperties = mapOf(PROPERTY_BASE_URL to BuildConfig.BASE_URL))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                TOKEN = token!!
                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                LogWrapper.log("TAG", msg)
            })
    }
}