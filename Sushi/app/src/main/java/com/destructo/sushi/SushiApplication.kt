package com.destructo.sushi

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Build
import androidx.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SushiApplication: Application() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        instance = this

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        createNotificationChannels()
    }

    private fun createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val updateChannelName = getString(R.string.notification_channel_update)
            val updateChannelDesc = getString(R.string.notification_channel_update_desc)
            val updateChannelImportance = NotificationManager.IMPORTANCE_DEFAULT
            val updateChannel = NotificationChannel(UPDATE_CHANNEL_ID, updateChannelName, updateChannelImportance)
            updateChannel.description = updateChannelDesc

            val promotionChannelName = getString(R.string.notification_channel_promotion)
            val promotionChannelDesc = getString(R.string.notification_channel_promotion_desc)
            val promotionChannelImportance = NotificationManager.IMPORTANCE_DEFAULT
            val promotionChannel = NotificationChannel(PROMOTION_CHANNEL_ID, promotionChannelName, promotionChannelImportance)
            promotionChannel.description = promotionChannelDesc

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(updateChannel)
            notificationManager.createNotificationChannel(promotionChannel)
        }

    }

    companion object {
        private var instance: SushiApplication? = null

        fun getContext(): SushiApplication {
            return instance!!
        }


    }


}