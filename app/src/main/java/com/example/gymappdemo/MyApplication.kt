package com.example.gymappdemo

import android.app.Application
import com.example.gymappdemo.data.AppContainer
import com.example.gymappdemo.data.AppContainerImpl

class MyApplication : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        // Initialize AppContainer after Application is fully initialized
        appContainer = AppContainerImpl(this)
    }
}
