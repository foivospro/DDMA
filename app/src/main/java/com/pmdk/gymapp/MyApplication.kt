package com.pmdk.gymapp

import android.app.Application
import com.pmdk.gymapp.data.AppContainer
import com.pmdk.gymapp.data.AppContainerImpl

class MyApplication : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        // Initialize AppContainer after Application is fully initialized
        appContainer = AppContainerImpl(this)
    }
}
