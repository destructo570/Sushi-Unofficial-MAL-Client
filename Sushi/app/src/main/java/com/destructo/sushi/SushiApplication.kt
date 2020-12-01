package com.destructo.sushi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SushiApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }



}